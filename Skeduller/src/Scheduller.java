import java.util.*;
import java.util.concurrent.Semaphore;

public class Scheduller {
    private LinkedList<Proceso> listaNuevosProcesos;
    private Queue<Proceso> colaLista;
    private LinkedList<Proceso> listaBloqueado;
    private LinkedList<Proceso> listaProcesosTerminados;
    private Proceso procesoEnEjecucion;
    private Semaphore semaforo;
    private List<IRecurso> recursosDisponibles;

    public Scheduller(List<IRecurso> recursos) {
        PriorityQueue<Proceso> cola = new PriorityQueue<>(Comparator.reverseOrder());
        this.listaNuevosProcesos = new LinkedList<>();
        this.listaProcesosTerminados = new LinkedList<>();
        this.colaLista = new LinkedList<>();
        this.listaBloqueado = new LinkedList<>();
        this.semaforo = new Semaphore(1);
        this.recursosDisponibles = recursos;
    }

    public void agregarProceso(Proceso proceso) {
        listaNuevosProcesos.offer(proceso);
    }

    public void ejecutarProcesos() throws InterruptedException {
        while (!listaNuevosProcesos.isEmpty()) {
            Proceso proceso = listaNuevosProcesos.poll();
            if (proceso.estado == Proceso.Estados.Nuevo) {
                proceso.estado = Proceso.Estados.Listo;
                colaLista.offer(proceso);
            } else if (proceso.estado == Proceso.Estados.Bloqueado) {
                listaBloqueado.offer(proceso);
                semaforo.wait(proceso.tiempoEjecucion); //?
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
                    Thread.sleep(proceso.tiempoEjecucion - currentTime); // duracion
                } catch (InterruptedException e) {
                    // Ignore interrupted exception
                }
            } else { // Run the next task
                Boolean recursoDisponible = true; // Si el recurso esta disponible
                for (IRecurso recur : proceso.recursosUsados){
                    if (recur.getEstado() == true){
                        recursoDisponible = false;
                    }
                }
                if (recursoDisponible == false){
                    colaDeEspera.add(proceso);
                }else{
                    proceso.run();
                    colaLista.poll();
                    if (proceso.estado == Proceso.Estados.Ejecucion) {
                        proceso.estado = Proceso.Estados.Listo;
                        colaLista.offer(proceso);
                    } else if (proceso.estado == Proceso.Estados.Bloqueado) {
                        for (IRecurso recursos : proceso.recursosUsados) {
                            recursos.cambiarEstadoUsando();
                        }
                        semaforo.acquire();
                    } else if (proceso.estado == Proceso.Estados.Terminado) {
                        for (IRecurso recursos : proceso.recursosUsados) {
                            recursos.cambiarEstadoDisponible();
                        }
                    }
                }
            }
        }
    }

    public void solicitarRecurso(Proceso proceso) {
        boolean disponible = true;
        for (IRecurso r: proceso.recursosUsados) {
            if (r.getEstado() == true){
                disponible = false;
            }
        }
        if (disponible != false){
            for (IRecurso r: proceso.recursosUsados) {
                r.cambiarEstadoUsando();
            }
        }else{
            proceso.estado = Proceso.Estados.Bloqueado;
        }
    }

    public void liberarRecurso(Proceso proceso) {
        for (IRecurso r: proceso.recursosUsados) {
            r.cambiarEstadoDisponible();
        }
        proceso.estado = Proceso.Estados.Terminado;
        colaLista.remove(proceso);
    }

    public void wait(Proceso proceso) throws InterruptedException {
        semaforo.wait(proceso.tiempoEjecucion);
    }

    public void signal(Proceso proceso) {
        semaforo.signal(proceso);
    }


}

