import java.util.*;
import java.util.concurrent.Semaphore;

public class Scheduller {
    private Queue<Proceso> colaDeTrabajos;
    private Queue<Proceso> colaLista;
    private Queue<Proceso> colaDeEspera;
    private Semaphore semaforo;
    private List<IRecurso> recursosDisponibles;

    public Scheduller(List<IRecurso> recursos) {
        PriorityQueue<Proceso> cola = new PriorityQueue<>(Comparator.reverseOrder());
        this.colaDeTrabajos = new LinkedList<>();
        this.colaLista = new LinkedList<>();
        this.colaDeEspera = new LinkedList<>();
        this.semaforo = new Semaphore(1);
        this.recursosDisponibles = recursos;
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
                    if (proceso.estado == Proceso.Estados.Correr) {
                        proceso.estado = Proceso.Estados.Listo;
                        colaLista.offer(proceso);
                    } else if (proceso.estado == Proceso.Estados.Bloquear) {
                        for (IRecurso recursos : proceso.recursosUsados) {
                            recursos.cambiarEstadoUsando();
                        }
                        semaforo.acquire();
                    } else if (proceso.estado == Proceso.Estados.Finalizar) {
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
            proceso.estado = Proceso.Estados.Bloquear;
        }
    }

    public void liberarRecurso(Proceso proceso) {
        for (IRecurso r: proceso.recursosUsados) {
            r.cambiarEstadoDisponible();
        }
        proceso.estado = Proceso.Estados.Finalizar;
            colaLista.offer(proximoProceso);
        }
    }

    public void wait(Proceso proceso) {
        semaforo.wait(proceso);
    }

    public void signal(Proceso proceso) {
        semaforo.signal(proceso);
    }


}

