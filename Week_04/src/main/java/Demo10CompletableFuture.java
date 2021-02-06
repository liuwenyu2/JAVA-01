public class Demo10CompletableFuture extends Fibo{
    private static volatile int value = 0;

    public int sum() {
        return fibo(36);
    }
}
