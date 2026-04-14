import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerDemo {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();   // 队列不满，生产者等/被唤醒的地方
    private final Condition notEmpty = lock.newCondition();  // 队列不空，消费者等/被唤醒的地方

    public void produce(int value) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                System.out.println(Thread.currentThread().getName() + " 发现队列满了，进入等待");
                notFull.await();
            }

            queue.offer(value);
            System.out.println(Thread.currentThread().getName() + " 生产了: " + value + "，当前队列大小: " + queue.size());

            // 放入一个元素后，队列一定“可能不空了”，该唤醒消费者
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " 发现队列空了，进入等待");
                notEmpty.await();
            }

            int value = queue.poll();
            System.out.println(Thread.currentThread().getName() + " 消费了: " + value + "，当前队列大小: " + queue.size());

            // 取出一个元素后，队列一定“可能不满了”，该唤醒生产者
            notFull.signal();

            return value;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ProducerConsumerDemo demo = new ProducerConsumerDemo();

        // 生产者线程
        Runnable producerTask = () -> {
            int value = 1;
            while (true) {
                try {
                    demo.produce(value++);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        // 消费者线程
        Runnable consumerTask = () -> {
            while (true) {
                try {
                    demo.consume();
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        new Thread(producerTask, "生产者-1").start();
        new Thread(producerTask, "生产者-2").start();
        new Thread(consumerTask, "消费者-1").start();
        new Thread(consumerTask, "消费者-2").start();
    }
}