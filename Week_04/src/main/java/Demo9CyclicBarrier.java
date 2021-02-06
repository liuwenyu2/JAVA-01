import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Demo9CyclicBarrier extends Fibo{
    private static volatile int value = 0;
    CyclicBarrier barrier = new CyclicBarrier(1,()->{sum();});

    public int get() {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void sum(){
        value = fibo(35);
    }
}
