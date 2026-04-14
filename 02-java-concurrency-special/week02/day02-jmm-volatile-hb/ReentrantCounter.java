import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantCounter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();
    public void inc() {
        lock.lock(); // 手动加锁
        try {
            count++;
        } finally {
            lock.unlock(); // 必须 finally 释放****************
            //******************一定得释放
        }
    }
    public int get() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantCounter c = new ReentrantCounter();
                Thread t1 = new Thread(() -> {
                    for (int i = 0; i < 10; i++) {
                         c.inc();
                    }
                });
                Thread t2 = new Thread(() -> {
                    for (int i = 0; i < 10; i++) {
                        c.inc();
                    }
                });
                t1.start();
                t2.start();
                t1.join();
                t2.join();
                System.out.println(c.get());
    }
}
