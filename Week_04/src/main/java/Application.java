import java.util.concurrent.*;

public class Application {
    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        //1:利用while获取线程结果
        Demo1While demoWhile = new Demo1While();
        Thread tWhile = new Thread(() -> {
            demoWhile.sum();
        });
        tWhile.start();

        int result1 = demoWhile.get();
        print("1.While异步计算结果为：", result1, start);

        //2：利用Join获取线程结果
        start = System.currentTimeMillis();
        Demo2Join demoJoin = new Demo2Join();
        Thread tJion = new Thread(() -> {
            demoJoin.sum();
        });
        tJion.start();
        tJion.join();//直到线程结束，才继续往下执行

        int result2 = demoJoin.get();
        print("2.Join异步计算结果为：", result2, start);

        //3：利用Synchronized获取线程结果
        start = System.currentTimeMillis();
        Demo3Synchronized demoSynchronized = new Demo3Synchronized();
        Thread tSynchronized = new Thread(() -> {
            demoSynchronized.sum();
        });
        tSynchronized.start();

        int result3 = demoSynchronized.get();
        print("3.Synchronized异步计算结果为：", result3, start);

        //4：利用Sleep获取线程结果
        start = System.currentTimeMillis();
        Demo4Sleep demo4Sleep = new Demo4Sleep();
        Thread tSleep = new Thread(() -> {
            demo4Sleep.sum();
        });
        tSleep.start();

        int result4 = demo4Sleep.get();
        print("4.sleep异步计算结果为：", result4, start);

        //5：利用Semaphore获取线程结果
        start = System.currentTimeMillis();
        Demo5Semaphore demo5Semaphore = new Demo5Semaphore();
        Thread t5Semaphore = new Thread(() -> {
            demo5Semaphore.sum();
        });
        t5Semaphore.start();

        int result5 = demo5Semaphore.get();
        print("5.Semaphore异步计算结果为：", result5, start);

        //6：利用ReentrantLock获取线程结果
        start = System.currentTimeMillis();
        Demo6ReentrantLock demo6ReentrantLock = new Demo6ReentrantLock();
        Thread t6ReentrantLock = new Thread(() -> {
            demo6ReentrantLock.sum();
        });
        t6ReentrantLock.start();

        int result6 = demo6ReentrantLock.get();
        print("6.ReentrantLock异步计算结果为：", result6, start);

        //7：利用CountDownLatch获取线程结果
        start = System.currentTimeMillis();
        Demo7CountDownLatch demo7CountDownLatch = new Demo7CountDownLatch();
        Thread t7CountDownLatch = new Thread(() -> {
            demo7CountDownLatch.sum();
        });
        t7CountDownLatch.start();

        int result7 = demo7CountDownLatch.get();
        print("7.CountDownLatch异步计算结果为：", result7, start);

        //8：利用FutureTask获取线程结果
        start = System.currentTimeMillis();
        Demo8Future demo8Future = new Demo8Future();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
            demo8Future.sum();
            return demo8Future.get();
        });

        new Thread(futureTask).start();

        int result8 = 0;
        try {
            result8 = futureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        print("8.FutureTask异步计算结果为：", result8, start);

        //9：利用CyclicBarrier获取线程结果
        start = System.currentTimeMillis();
        Demo9CyclicBarrier demo9CyclicBarrier = new Demo9CyclicBarrier();
        Thread t9CyclicBarrier = new Thread(() -> {
            demo9CyclicBarrier.sum();
        });
        t9CyclicBarrier.start();

        int result9 = demo9CyclicBarrier.get();
        print("9.CyclicBarrier异步计算结果为：", result9, start);

        //10：利用CompletableFuture获取线程结果
        start = System.currentTimeMillis();
        Demo10CompletableFuture demo10CompletableFuture = new Demo10CompletableFuture();
        Integer result = CompletableFuture.supplyAsync(() -> demo10CompletableFuture.sum()).join();
        print("10.CompletableFuture异步计算结果为：", result, start);

        //11：利用Future获取线程结果
        start = System.currentTimeMillis();
        Demo8Future demo11Future = new Demo8Future();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(() -> {
            demo11Future.sum();
            return demo11Future.get();
        });
        executorService.shutdown();

        int result11 = 0;
        try {
            result11 = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        print("11.Future异步计算结果为：", result11, start);
    }

    private static void print(String demoName, int result, long start) {
        System.out.println(demoName + result);
        System.out.println("  使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }
}
