import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Scheduller {
    private Queue<Proceso> colaDeTrabajos;
    private Queue<Proceso> colaLista;
    private Queue<Proceso> colaDeEspera;
    private Semaphore semaforo;
    private List<IRecurso> recursos;

    public Scheduller(List<IRecurso> recursos) {
        this.colaDeTrabajos = new LinkedList<>();
        this.colaLista = new LinkedList<>();
        this.colaDeEspera = new LinkedList<>();
        this.semaforo = new Semaphore(10);
        this.recursos = recursos;
    }

    public void agregarProceso(Proceso proceso) {
        colaDeTrabajos.offer(proceso);
    }

    public void ejecutarProcesos() throws InterruptedException {
        while (!colaDeTrabajos.isEmpty()) {
            Proceso proceso = colaDeTrabajos.poll();
            if (proceso.estado == Proceso.Estados.Nuevo) {
                colaLista.offer(proceso);
            } else if (proceso.estado == Proceso.Estados.Bloquear) {
                colaDeEspera.offer(proceso);
                semaforo.wait(proceso.tiempoEjecucion);
            }
        }
        run();
    }

    public void run() throws InterruptedException {
        while (!colaLista.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            Proceso proceso = colaLista.peek();
            if (proceso.tiempoEjecucion > currentTime) {
                // Sleep until it's time for the next task
                try {
                    Thread.sleep(proceso.tiempoEjecucion - currentTime);
                } catch (InterruptedException e) {
                    // Ignore interrupted exception
                }
            } else {
                // Run the next task
                proceso.run();
                colaLista.poll();
                if (proceso.estado == Proceso.Estados.Correr) {
                    proceso.estado = Proceso.Estados.Listo;
                    colaLista.offer(proceso);
                } else if (proceso.estado == Proceso.Estados.Bloquear) {
                    for (IRecurso recursos : proceso.recursos) {
                        recursos.cambiarEstadoUsoando();
                    }
                    semaforo.acquire();
                } else if (proceso.estado == Proceso.Estados.Finalizar) {
                    for (IRecurso recursos : proceso.recursos) {
                        recursos.cambiarEstadoDisponible();
                    }
                }
            }
        }
    }

    // Métodos de gestión de recursos y semáforos
    // ...

}

