import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {
    private LinkedList<Integer> queue = new LinkedList<>();

    private int max=5;

    private final ReentrantLock lock = new ReentrantLock();//必须是pricate finall。不然会被外部干扰
    private final Condition notFull = lock.newCondition();  // 生产者等
    private final Condition notEmpty = lock.newCondition(); // 消费者等

    public void put(int v) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == max) {
                notFull.await();
            }
            queue.offer(v);
            System.out.println("生产 " + v + " 队列长度 " + queue.size());
            notEmpty.signal();
        }
        finally {
            lock.unlock();
        }
    }
    public void take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            int v = queue.poll();//
            System.out.println("消费 " + v + " 队列长度 " + queue.size());
            notFull.signal();
        }
        finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ProducerConsumer producerConsumer = new ProducerConsumer();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    producerConsumer.put(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    producerConsumer.take();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
