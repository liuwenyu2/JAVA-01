1、字节码是什么
Java 中字节码英文为bytecode，由单字节（byte）的指令组成，理论上最多支持2的8次方（256）个操作码，但java中只使用200左右的操作码，且这些字节码作为JVM的指令集，供JVM加载并执行（换句话说java字节码就是JVM执行的指令格式，就像汇编语言和机器语言一样）

补充：操作码主要由类型前缀和操作名称组成，根据指令性质，主要分为4类：栈操作指令；程序流程控制指令；对象操作指令；运算以及类型转换指令

2、为什么要用字节码
此处为什么要用字节码要分两个维度：

第一个维度：为什么要用字节码来实现？

可跨平台，且JVM只执行字节码指令；
字节码解析速度快；
字节码size压缩更小；
格式版本稳定；
能做字节码保护；
第二个维度：为什么要了解掌握字节码？

可以排查和分析问题

可以免编译直接通过修改字节码调整程序逻辑

为了解分析器，AOP等工具提供更好的基础

3、怎么看字节码
在怎么用字节码之前，我们先看看如何通过class文件来看字节码，获取字节码清单可以通过javap工具。javap是标准JDK内置的一款工具来专门反编译class文件。

JVM指令主要分为：本地变量表到操作数栈类指令、操作数栈到本地变量表类指令、常数到操作数栈类指令、将数组指定索引的数组推送至操作数栈类指令、将操作数栈数存储到数组指定索引类指令、操作数栈其他相关类指令、运算相关类指令、条件转移类指令、类和数组类指令和其他指令。
i开头的指令操作数类型是integer类型，l开头的指令操作数类型是long类型，f开头的指令操作数类型是float类型，d开头的指令操作数类型是double，a开头的指令操作数类型是引用类型（reference）。
load类指令将数据从本地变量表加载到操作数栈，store类指令将数据从操作数栈存储到本地变量表中。其他的指令主要用于操作数栈。