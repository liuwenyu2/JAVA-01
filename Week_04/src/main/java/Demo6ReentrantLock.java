import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Demo6ReentrantLock extends Fibo {
    private static volatile int value = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public void sum() {
        lock.lock();
        value = fibo(36);
        condition.signal();
        lock.unlock();
    }

    public int get() {
        lock.lock();
        while (value == 0) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.unlock();
        return value;
    }
}
