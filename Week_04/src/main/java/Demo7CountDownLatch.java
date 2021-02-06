import java.util.concurrent.CountDownLatch;

public class Demo7CountDownLatch extends Fibo{
    private static volatile int value = 0;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public void sum() {
        value = fibo(36);
        countDownLatch.countDown();
    }

    public int get() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }
}
