public class ThreadDemo {
    // 定义线程任务（Lambda 简化写法）
    Runnable task = () -> {
        // 打印当前执行任务的线程名称
        System.out.println("当前线程：" + Thread.currentThread().getName());
    };

    // 程序入口：必须放在 main 方法中执行
    public static void main(String[] args) {
        ThreadDemo demo = new ThreadDemo();

        System.out.println("===== 1. 直接调用 run()：无新线程 =====");
        // 普通方法调用，执行在 main 线程
        demo.task.run();

        System.out.println("===== 2. 调用 start()：创建新线程 =====");
        // 把任务交给 Thread 类 → 真正创建线程
        Thread thread = new Thread(demo.task);
        thread.setName("我的新线程");
        thread.start(); // 启动新线程（JVM 自动调用 run()）
    }
}