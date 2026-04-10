
    import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    public class ExecutorsOOMDemo {
        public static void main(String[] args) {
            // 1. 使用 Executors 创建一个只有 2 个线程的线程池
            // 弊端：它底层默默为你创建了一个容量为 Integer.MAX_VALUE 的“无界队列”
            ExecutorService executor = Executors.newFixedThreadPool(2);

            System.out.println("==== 模拟双十一流量洪峰，开始疯狂提交任务 ====");

            try {
                // 2. 死循环，疯狂向线程池提交任务
                for (int i = 1; ; i++) {

                    // 为了加速 OOM 现象，我们在每个任务中模拟携带 1MB 的数据报文
                    final byte[] requestPayload = new byte[1024 * 1024];

                    executor.execute(() -> {
                        try {
                            // 3. 模拟业务处理非常缓慢（例如发生了慢 SQL，卡住 10 秒）
                            // 这意味着 2 个线程根本处理不过来，后面的任务全部塞进无界队列排队
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 打印占位符，防止变量被 JVM 优化掉
                        System.out.print("");
                    });

                    if (i % 50 == 0) {
                        System.out.println("已疯狂提交并堆积了 " + i + " 个任务...");
                    }
                }
            } catch (Throwable e) {
                // 4. 捕捉内存溢出异常并打印
                System.err.println("==== 轰！系统崩溃了 ====");
                System.err.println("报错信息: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


