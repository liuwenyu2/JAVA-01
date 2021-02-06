public class Demo4Sleep extends Fibo {

    private static volatile int value = 0;

    public void sum() {
        value = fibo(36);
    }

    public int get() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }
}