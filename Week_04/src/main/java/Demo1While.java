public class Demo1While extends Fibo{

    private static volatile int value = 0;

    public void sum(){
        value = fibo(36);
    }

    public int get() {
        while (true) {
            if (value != 0) {
                return value;
            }
        }
    }
}
