import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDEMON

{
    public  static void main(String[] args)
    {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2, 4,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2)
        );

        for (int i = 0; i < 6; i++) {
            int num = i;
//            pool.execute(() -> {
//                System.out.println(
//                        Thread.currentThread().getName() + " 执行任务 " + num
//                );
//            });
            pool.execute(() -> {
                try {
                    Thread.sleep((long)(Math.random() * 1000));
                } catch (Exception e) {}

                System.out.println(Thread.currentThread().getName());
            });
        }
    }
}
