public class Test {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.add();
            }
        }, "线程A");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.add();
            }
        }, "线程B");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(counter.count);
    }
}

class Counter {
    int count = 0;

    public void add() {
        count++;
    }
}