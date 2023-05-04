import java.util.*;
import java.util.concurrent.Semaphore;

public class Scheduller {
    private PriorityQueue<Proceso> colaLista;
    private LinkedList<Proceso> listaBloqueado;
    private LinkedList<Proceso> listaProcesosTerminados;
    private Semaphore semaforo;
    private List<IRecurso> recursosDisponibles;

    public Scheduller(List<IRecurso> recursos) {
        this.colaLista = new PriorityQueue<>();
        this.listaBloqueado = new LinkedList<>();
        this.listaProcesosTerminados = new LinkedList<>();
        this.semaforo = new Semaphore(1);
        this.recursosDisponibles = recursos;
    }

    public void agregarProceso(Proceso proceso) throws InterruptedException {
        if (proceso.estado == Proceso.Estados.Listo) {
            colaLista.offer(proceso);
        } else if (proceso.estado == Proceso.Estados.Bloqueado) {
            listaBloqueado.offer(proceso);
            semaforo.wait(proceso.tiempoEjecucion);
        }
    }

    public void ejecutarProcesos() throws InterruptedException {
        while (!colaLista.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            Proceso proceso = colaLista.peek();
            // Run the task
            if (solicitarRecurso(proceso) == true) { // solicita recursos y verifica disponibilidad
                if (proceso.tiempoEjecucion > currentTime) {
                    // Sleep until it's time for the next task
                    try {
                        Thread.sleep(proceso.tiempoEjecucion - currentTime); // duracion
                        proceso.run();
                    } catch (InterruptedException e) {
                        // Ignore interrupted exception
                    }
                }
                liberarRecurso(proceso);
            } else { // checkeo los bloqueados, para ver si van denuevos a la cola lista

            }
        }
    }


    public boolean solicitarRecurso(Proceso proceso) {
        boolean disponible = true;
        for (IRecurso r: proceso.recursosUsados) {
            if (r.siendoUsado() == true){
                disponible = false;
            }
        }
        if (disponible != false){
            for (IRecurso r: proceso.recursosUsados) {
                r.cambiarEstadoUsando();
            }
        }else{
            colaLista.poll();
            proceso.estado = Proceso.Estados.Bloqueado;
            listaBloqueado.add(proceso);
        }
        return disponible;
    }

    public void liberarRecurso(Proceso proceso) {
        for (IRecurso r: proceso.recursosUsados) {
            r.cambiarEstadoDisponible();
        }
        proceso.estado = Proceso.Estados.Terminado;
        colaLista.poll();
        listaProcesosTerminados.add(proceso);
    }
}

