import java.util.concurrent.Semaphore;

public class Demo5Semaphore extends Fibo {

    private static volatile int value = 0;
    private final static Semaphore semaphore = new Semaphore(1);

    public Demo5Semaphore() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sum() {
        value = fibo(36);
        semaphore.release();
    }

    public int get() {
        //在此处进行了抢占
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
        return value;
    }
}
