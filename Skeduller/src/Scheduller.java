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
            long tiempoRestante = proceso.tiempoEjecucion - currentTime;
            // Run the task
            if (tiempoRestante <= 0 && solicitarRecurso(proceso) == true) { //Si el tiempo restante es negativo el proceso debe ejecutarse de inmediato
                semaforo.acquire(); // adquirimos el semáforo antes de ejecutar el proceso
                proceso.run();
                liberarRecurso(proceso);
                semaforo.release(); // liberamos el semáforo para permitir que otro proceso lo adquiera
            }else if (tiempoRestante > 0 && solicitarRecurso(proceso) == true){
                semaforo.acquire(); // adquirimos el semáforo antes de ejecutar el proceso
                proceso.run();
                for (IRecurso r: proceso.recursosUsados) {
                    r.cambiarEstadoDisponible();
                }
                colaLista.poll();
                proceso.tiempoEjecucion -= currentTime;
                colaLista.offer(proceso);
                semaforo.release(); // liberamos el semáforo para permitir que otro proceso lo adquiera
            }else{ // poner en lista de bloqueados

            }
            // checkeo los bloqueados, para ver si van denuevo a la cola lista
            List<Proceso> procesosParaEliminar = new ArrayList<>();
            for (Proceso p: listaBloqueado) {
                boolean disponible = true;
                for (IRecurso recursos : p.recursosUsados){
                    if(recursos.siendoUsado()) {
                        disponible = false;
                        break;
                    }
                }
                if (disponible) {
                    p.estado = Proceso.Estados.Listo;
                    colaLista.add(p);
                    procesosParaEliminar.add(p);
                }
            }
            listaBloqueado.removeAll(procesosParaEliminar);
        }
    }

    public boolean solicitarRecurso(Proceso proceso) {
        boolean disponible = true;
        for (IRecurso r: proceso.recursosUsados) {
            if (r.siendoUsado()){
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


