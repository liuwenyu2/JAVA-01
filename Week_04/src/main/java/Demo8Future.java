public class Demo8Future extends Fibo{
    private static volatile int value = 0;

    public void sum() {
        value = fibo(36);
    }

    public int get() {
        return value;
    }
}
