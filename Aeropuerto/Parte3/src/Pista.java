import java.util.concurrent.Semaphore;

public class Pista {
    protected Semaphore usar;
    protected String nombre;

    public Pista(Semaphore semaforo, String nombre) {
        this.usar = semaforo;
        this.nombre = nombre;
    }


}
