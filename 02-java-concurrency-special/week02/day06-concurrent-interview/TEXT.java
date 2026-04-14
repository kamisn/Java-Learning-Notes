import java.util.concurrent.CountDownLatch;

public class TEXT {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + " 完成任务");
                    latch.countDown();
                }).start();
            }

latch.await(); // 等待三个任务完成
System.out.println("所有任务完成，主线程继续执行");


    }
}
