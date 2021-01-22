# 垃圾回收总结

## 1、Serial GC（复制算法）

Serial 回收器为最古老的回收器，是一个单线程的，并且他进行收集时需要STW，该回收器采用复制算法进行垃圾收集，具体工作原理图如下：

![image-20210121160450655](C:\Users\DK\AppData\Roaming\Typora\typora-user-images\image-20210121160450655.png)

**优点**：实现简单高效（与其他回收器单线程对比），对于限定单个CPU的环境中，Serial回收器没有线程交换的开销，专心做垃圾收集可以获得最高的单线程收集效率

**缺点**：给用户带来的延迟比较长

**使用场景**：Client模式；单核服务器

可以用-XX:+UseSerialGC参数指定年轻代和老年代都使用Serial GC，等价年轻代用Serial GC，老年代用Serial Old GC

**总结**：这种垃圾回收器了解即可，使用场景基本上很少使用，并且限定在单核CPU才能发挥其价值，但目前线上都是多核

## 2、Serial Old GC（标记-整理算法）

该收集器是老年代单线程回收器，是运行在Client模式下的默认的老年代垃圾回收器，其使用的是标记-整理算法。Serial Old在Server模式下主要有两个用途：

1. 与年轻代的Parallel Scavenge配合使用

2. 作为老年代CMS回收器的后备方案

   如下是Serial和Serial Old结合的原理图：

   ![image-20210121162017046](C:\Users\DK\AppData\Roaming\Typora\typora-user-images\image-20210121162017046.png)

   **优点和缺点**：见Serial GC，和Serial GC的区别就在于其主要处理的是老年代

   **使用场景**：Client模式；单核服务器；与ParallelScavenge GC搭配；作为CMS的备选方案

## 3、ParNew GC（复制算法）

ParNew GC是Serial GC的多线程版本，Par是Parallel的缩写，New：表示只能处理年轻代，所以该回收器为年轻代的并行回收器，此回收器采用复制算法

如下是ParNew和Serial Old结合的回收器过程图：

![image-20210121162627673](C:\Users\DK\AppData\Roaming\Typora\typora-user-images\image-20210121162627673.png)



**优点**：多线程处理可以更高效的利用CPU资源，固然比Serial更高效

**缺点**：在单CPU环境下，由于CPU需要频繁的切换任务，导致额外的开销

**使用场景**：多核服务器；与CMS回收器配合使用；与Serial Old配合使用

说明：当配置-XX:+UseConcMarkSweepGC来选择CMS作为老年代回收器时，年轻代默认为ParNew，也可以用-XX:UseParNewGC来指定使用PerNew作为年轻代回收器

-XX:ParallerGCThreads限制线程数量，默认开启和CPU数据相同的线程数



## 4、Parallel Scavenge GC（复制算法）

Parallel ScavengeGC也是并行回收，同样采用复制算法，它和ParNew的不同在于如下两点：

1. Parallel Scavenge目标是达到一个可控的吞吐量，可以称之为吞吐量优先的回收器
2. 自适应调节策略

如下是Parallel Scavenge GC和Parallel Old结合的示意图：

![image-20210121163931172](C:\Users\DK\AppData\Roaming\Typora\typora-user-images\image-20210121163931172.png)



**优点**：可控吞吐量；具有自适应调节策略（但是这个自适应调节策略是什么呢？？）

**缺点**：注重吞吐量则导致处理效率低，相应不及时

**适用场景**：适合后台应用对交互相应需求不高的场景，执行批量处理；订单处理；工资支付；科学计算的应用程序等



## 5、Parallel Old GC（标记-整理算法）

老年代并行回收器，也是吞吐量优先，是Parallel Scavenge回收器的老年代版本；

**使用场景**：与Parallel Scavenge收集器搭配使用；注重吞吐量。jdk7、jdk8 默认使用该收集器作
为年代收集器，使用 -XX:+UseParallelOldGC 来指定Paralle Old 收集器。

## 6、CMS GC（标记-清除算法）

CMS全称为：Concurrent Mark Sweep，是一种尽可能低延迟收集器，他是第一次严格意义上的和用户线程并行处理的一款GC，且重点是回收老年代GC

CMS收集过程如下：

### 6.1 Initial Mark（初始标记）

这个阶段伴随着 STW 暂停。初始标记的目标是标记所有的根对象，包括根对象直接引用的对象，以及被年轻代中所有存活对象所引用的对象（老年代单独回收）。

### 6.2 Concurrent Mark（并发标记）

在此阶段，CMS GC 遍历老年代，标记所有的存活对象，从前一阶段 “Initial Mark” 找到的根对象开始算起。 “并发标记”阶段，就是与应用程序同时运行，不用暂停的阶段。

### 6.3 Concurrent Preclean（并发预清理）

此阶段同样是与应用线程并发执行的，不需要停止应用线程。 因为前一阶段【并发标记】与程序并发运行，可能有一些引用关系已经发生了改变。如果在并发标记过程中引用关系发生了变化，JVM 会通过“Card（卡片）”的方式将发生了改变的区域标记为“脏”区，这就是所谓的 卡片标记（Card Marking）。

### 6.4 Final Remark（最终标记）

最终标记阶段是此次 GC 事件中的第二次（也是最后一次）STW 停顿。本阶段的目标是完成老年代中所有存活
对象的标记. 因为之前的预清理阶段是并发执行的，有可能 GC 线程跟不上应用程序的修改速度。所以需要一次
STW 暂停来处理各种复杂的情况。通常 CMS 会尝试在年轻代尽可能空的情况下执行 Final Remark 阶段，以免连续触发多次 STW 事件。

### 6.5 Concurrent Sweep（并发清除）

此阶段与应用程序并发执行，不需要 STW 停顿。JVM 在此阶段删除不再使用的对象，并回收他们占用的内存空间。

### 6.6 Concurrent Reset（并发重置）

此阶段与应用程序并发执行，重置 CMS 算法相关的内部数据，为下一次 GC 循环做准备。



CMS 垃圾收集器在减少停顿时间上做了很多复杂而有用的工作，用于垃圾回收的并发线程执行的同时，并不需要暂停应用线程。 当然，CMS 也有一些缺点，其中最大的问题就是老年代内存碎片问题（因为不压缩），在某些情况下 GC 会造成不可预测的暂停时间，特别是堆内存较大的情况下。

**优点**：并发收集；低延迟

**缺点**：对CPU资源非常敏感；无法处理浮动的垃圾；存在很多内存碎片

**适用场景**：多核；低延时场景

**总结**：

如果想要最小化地使用内存和并行开销，请选择Serial Old(老年代) + Serial(年轻代)
如果想要最大化应用程序的吞吐量，请选择Parallel Old(老年代) + Parallel(年轻代)
如果想要最小化GC的低延迟，请选择CMS(老年代) + ParNew(年轻代)

JDK 10+以后CMS GC将被G1替代

## 7、G1 GC（区域化分代式算法）

既然已经有前面几个强大的GC，为什么还要发布G1 GC呢？

原因在于应用程序对应业务越来越庞大、复杂、用户越来越多，而如上总多GC中的STW跟不上实际需求，且如上每个GC都有相对于的缺点跟不上低延迟的要求小的高吞吐量，所有需要不断的优化GC，而G1 GC就是优化过程中的一个产物

官方给G1设定的目标是延迟可控的情况下尽可能的高吞吐量，它是一款面向服务端应用的收集器，能充分利用多CPU环境

### 为什么叫Garbage First呢？

因为G1是一个并行回收器，它把堆内存分割为很多不相关的区域(Region)（物理上是不连续的)。使用不同的Region来表示Eden、survivor、old等。G1通过跟踪各个Region里面的垃圾堆积的价值大小，在后台维护一个优先列表，每次根据容许收集的时间，优先回收价值最大的Region，由于这种方式侧重点在于回收垃圾量最大的Region所以就命名为垃圾优先

**优点**：同事并行和并发；分代收集；可预测STW时间模型；

**缺点**：小内存，CMS性能高于G1；大内存（6~8G）G1更胜一筹

### G1回收器的参数设置

-XX:+UseG1GC开启G1GC

-XX:G1HeapRegionSize设置每个Region大小，值为2的幂，范围1MB~32MB，目标是根据最小Java堆大小划分出约2048个区域，默认是堆内存的1/2000。

-XX:MaxGCPauseMills设置期望达到的最大GC停顿时间默认是200ms

-XX:InitiatingHeapOccupancyPercent 设置触发GC周期的java堆占用阀值，超过则触发GC，默认是45

-XX:ParallerGCThread设置STW工作线程，最多设置为8

-XX:ConcGCThreads设置并发标记的线程数，设置CPU数量的1/4左右

### 如何设置

G1的设计原则就是简化JVM性能调优，只需要简单三步即可完成：
第一步: 开启G1垃圾收集器 （-XX:+UseG1GC）
第二步:设置堆的最大内存( -Xmx -Xms)

第三步:设置最大的停顿时间(‐XX:MaxGCPauseMillis)

### 使用场景

1. 面向服务端应用，具有大内存、多处理器
2. 有低延迟需求的
3. 在堆大小约6GB或更大时，可预测的暂停时间可以低于0.5秒；(G1通过每次只清理部分而不是全部
   Region的增量式清理在保证每次GC停顿时间不会过长) 。
4. 用来来替换掉JDK1.5中的CMS收集器，以下情况，使用G1可能比CMS好
   超过50% 的java堆被活动数据占用;
   对象分配频率或年代提升频率变化很大；
   GC停顿时间过长(大于0.5~1秒)
5. HotSpot垃圾收集器里，除了G1以外，其他的垃圾收集器使用内置的JVM线程执行GC多线程操作，
   而G1 GC可以采用应用线程运用GC的工作，即当JVM的GC线程处理速度慢时，系统会调用应用程序帮
   助加速垃圾回收过程。

各个GC汇总说明如下：

![image-20210121175105666](C:\Users\DK\AppData\Roaming\Typora\typora-user-images\image-20210121175105666.png)