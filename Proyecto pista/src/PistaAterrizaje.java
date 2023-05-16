import java.util.concurrent.Semaphore;

class PistaAterrizaje {
    private Semaphore semaforo;

    public PistaAterrizaje() {
        semaforo = new Semaphore(1); // Solo un avión puede aterrizar a la vez
    }

    public void aterrizar(String avion) {
        try {
            semaforo.acquire(); // Intentar adquirir el permiso para aterrizar
            System.out.println(avion + " ha aterrizado.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    public void despegar(String avion) {
        System.out.println(avion + " ha despegado.");
        semaforo.release(); // Liberar el permiso para que otro avión pueda aterrizar
    }
}