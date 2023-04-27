import java.util.concurrent.Semaphore;
public class Semaforo {
    private int count;
    private Semaphore semaphore;
    public Semaforo(int initialCount) {
        this.count = initialCount;
    }

    public synchronized void acquire() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        count--;
    }

    public synchronized void release() {
        count++;
        notify();
    }
}
