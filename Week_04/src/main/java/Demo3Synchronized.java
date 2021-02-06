public class Demo3Synchronized extends Fibo{
    private static volatile int value = 0;

    public synchronized void sum(){
        value = fibo(36);
        this.notifyAll();
    }

    public synchronized int get() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }
}
