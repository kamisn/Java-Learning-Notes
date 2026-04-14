public class SyncCounter {
    private int count = 0;

        public int add() {
            synchronized (this) {
                count++;
            }
            return 0;
        }
    public int get() {
        synchronized (this) {
            return count;
        }
    }
    public static void main(String[] args) throws InterruptedException{
            SyncCounter counter = new SyncCounter();
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    counter.add();
                }
            });
            Thread t2 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    counter.add();
                }
            });
                t1.start();
                t2.start();
                t2.join();
                t1.join();


                System.out.println(counter.get());
    }
      }