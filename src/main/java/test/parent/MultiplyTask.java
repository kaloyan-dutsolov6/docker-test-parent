package test.parent;

public class MultiplyTask {
    public static int multiply(int x, int y) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return x * y;
    }
}
