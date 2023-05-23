import java.util.*;
import java.util.concurrent.Semaphore;

public class Scheduller {
    private PriorityQueue<Proceso> colaLista;
    private LinkedList<Proceso> listaBloqueado;
    private LinkedList<Proceso> listaSuspendidoListo;
    private LinkedList<Proceso> listaSuspendidoBloqueado;

    private LinkedList<Proceso> listaProcesosTerminados;
    private Semaphore semaforo;
    private List<IRecurso> recursosDisponibles;


    public Scheduller(List<IRecurso> recursos) {
        this.colaLista = new PriorityQueue<>();
        this.listaBloqueado = new LinkedList<>();
        this.listaSuspendidoListo = new LinkedList<>();
        this.listaSuspendidoBloqueado = new LinkedList<>();
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
                proceso.runMax();
                for (IRecurso r: proceso.recursosUsados) {
                    r.cambiarEstadoDisponible();
                }
                colaLista.poll();
                proceso.tiempoEjecucion -= currentTime;
                colaLista.offer(proceso);
                semaforo.release(); // liberamos el semáforo para permitir que otro proceso lo adquiera
            }
            // checkeo los bloqueados, para ver si van denuevo a la cola lista
            List<Proceso> procesosParaEliminar = new ArrayList<>();
            for (Proceso p: listaBloqueado) {
                salidaBolqueado(p,procesosParaEliminar);
            }
            listaBloqueado.removeAll(procesosParaEliminar);

            List<Proceso> procesosParaSuspender = new ArrayList<>();
            for (Proceso p: listaSuspendidoListo) {
                salidaSuspendidoListo(p,procesosParaSuspender);
            }
            listaSuspendidoListo.removeAll(procesosParaSuspender);
        }
    }

    public void salidaBolqueado(Proceso proceso, List<Proceso> procesosParaEliminar){
        boolean disponible = true;
        boolean ruptura = false;
        for (IRecurso recursos : proceso.recursosUsados){
            if (recursos.getEstaRoto()){
                ruptura = true;
            }
            if(recursos.siendoUsado()) {
                disponible = false;
                break;
            }
        }
        if (ruptura && disponible == false){
            proceso.estado = Proceso.Estados.SuspendidoBloqueado;
            listaSuspendidoBloqueado.add(proceso);
            procesosParaEliminar.add(proceso);
            return;
        }
        if (disponible && ruptura == false) {
            proceso.estado = Proceso.Estados.Listo;
            colaLista.add(proceso);
            procesosParaEliminar.add(proceso);
        }
    }

    public void salidaSuspendidoListo(Proceso proceso, List<Proceso> procesosParaSuspender){
        Boolean ruptura = true;
        for (IRecurso recursos : proceso.recursosUsados){
            if (recursos.getEstaRoto() == false){
                ruptura = false;
            }
        }
        if (ruptura == false){
            listaSuspendidoListo.remove(proceso);
            proceso.estado = Proceso.Estados.Listo;
            colaLista.offer(proceso);
        }
    }

    public boolean solicitarRecurso(Proceso proceso) {
        boolean disponible = true;
        boolean ruptura = false;
        for (IRecurso r: proceso.recursosUsados) {
            if (r.siendoUsado()){
                disponible = false;
            }
            if (r.getEstaRoto()){
                ruptura = true;
            }
        }
        if (ruptura == true) {
            colaLista.poll();
            listaSuspendidoListo.add(proceso);
            proceso.estado = Proceso.Estados.SuspendidoListo;
            return !ruptura;
        }else if (disponible != false){
            for (IRecurso r: proceso.recursosUsados) {
                r.cambiarEstadoUsando();
            }
        }else{
            colaLista.poll();
            listaBloqueado.add(proceso);
            proceso.estado = Proceso.Estados.Bloqueado;
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


