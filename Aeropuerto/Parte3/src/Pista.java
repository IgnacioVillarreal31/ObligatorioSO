import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Pista {
    protected Semaphore usar;
    protected String nombre;

    protected ReentrantLock lock;

    public Pista(Semaphore semaforo, String nombre) {
        this.usar = semaforo;
        this.nombre = nombre;
        lock = new ReentrantLock();
    }

    public void bloquear() {
        lock.lock();
    }

    public void esperar(){
        lock.lock();

    }

    public void desbloquear() {
        lock.unlock();
    }

}
