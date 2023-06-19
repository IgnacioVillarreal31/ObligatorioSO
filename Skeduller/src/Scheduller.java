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
    public String ruta;


    //Inicializamos variables y el semáforo en 1 para que sólo un proceso
    //acceda a la vez.
    public Scheduller(List<IRecurso> recursos, String ruta) {
        this.colaLista = new PriorityQueue<>();
        this.listaBloqueado = new LinkedList<>();
        this.listaSuspendidoListo = new LinkedList<>();
        this.listaSuspendidoBloqueado = new LinkedList<>();
        this.listaProcesosTerminados = new LinkedList<>();
        this.semaforo = new Semaphore(1);
        this.recursosDisponibles = recursos;
        this.ruta = ruta;
    }


    //Agregamos todos los procesos a las listas y colas correspondientes
    //según su estado.
    public void agregarProceso(Proceso proceso) throws InterruptedException {
        if (proceso.estado == Proceso.Estados.Listo) {
            colaLista.offer(proceso);
        } else if (proceso.estado == Proceso.Estados.Bloqueado) {
            ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + proceso.id + " fue bloqueado...");
            listaBloqueado.offer(proceso);
        }
    }

    //Método principal en el que se ejecutan los procesos, si no se
    //pueden ejecutar pasan a las otras listas, bloqueándolos o
    //suspendiéndolos, o ambas.
    public void ejecutarProcesos() throws InterruptedException {
        while (!colaLista.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            Proceso proceso = colaLista.peek();
            long tiempoRestante = proceso.tiempoEjecucion - currentTime;
            if (tiempoRestante <= 0 && solicitarRecurso(proceso) == true) { //Si el tiempo restante es negativo el proceso debe ejecutarse de inmediato
                semaforo.acquire(); //Adquirimos el semáforo antes de ejecutar el proceso
                proceso.run();
                liberarRecurso(proceso);
                semaforo.release(); //Liberamos el semáforo para permitir que otro proceso lo adquiera
            }else if (tiempoRestante > 0 && solicitarRecurso(proceso) == true){
                semaforo.acquire(); //Adquirimos el semáforo antes de ejecutar el proceso
                proceso.runMax();
                for (IRecurso r: proceso.recursosUsados) {
                    r.cambiarEstadoDisponible();
                }
                colaLista.poll();
                proceso.tiempoEjecucion -= currentTime;
                colaLista.offer(proceso);
                semaforo.release(); //Liberamos el semáforo para permitir que otro proceso lo adquiera
            }
            //Checkeo los bloqueados, para ver si van denuevo a la cola lista
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

    //Chequeo de bloqueados, para sacarlos de este estado y pasarlos
    //a listo
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
            ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + proceso.id + " fue suspendido...");
            proceso.estado = Proceso.Estados.SuspendidoBloqueado;
            listaSuspendidoBloqueado.add(proceso);
            procesosParaEliminar.add(proceso);
            return;
        }
        if (disponible && ruptura == false) {
            ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + proceso.id + " pasa de bloqueado a listo...");
            proceso.estado = Proceso.Estados.Listo;
            colaLista.add(proceso);
            procesosParaEliminar.add(proceso);
        }
    }

    //Chequeo de suspendidosListo, para sacarlos de este estado y pasarlos
    //a listo
    public void salidaSuspendidoListo(Proceso proceso, List<Proceso> procesosParaSuspender){
        Boolean ruptura = true;
        for (IRecurso recursos : proceso.recursosUsados){
            if (recursos.getEstaRoto() == false){
                ruptura = false;
            }
        }
        if (ruptura == false){
            ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + proceso.id + " sale del estado suspendido y vuelve al estado listo...");
            listaSuspendidoListo.remove(proceso);
            proceso.estado = Proceso.Estados.Listo;
            colaLista.offer(proceso);
        }
    }

    //Chequea si los recursos de un proceso están disponibles para ser
    //ejecutado, sino lo pone en otros estados, ya sea bloqueado como,
    //suspendido bloqueado.
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
            ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + proceso.id + " fue suspendido...");
            colaLista.poll();
            listaSuspendidoListo.add(proceso);
            proceso.estado = Proceso.Estados.SuspendidoListo;
            return !ruptura;
        }else if (disponible != false){
            for (IRecurso r: proceso.recursosUsados) {
                r.cambiarEstadoUsando();
            }
        }else{
            ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + proceso.id + "fue bloqueado...");
            colaLista.poll();
            listaBloqueado.add(proceso);
            proceso.estado = Proceso.Estados.Bloqueado;
        }
        return disponible;
    }

    //Una vez finalizado un proceso, libera los recursos de este
    public void liberarRecurso(Proceso proceso) {
        for (IRecurso r: proceso.recursosUsados) {
            r.cambiarEstadoDisponible();
        }
        proceso.estado = Proceso.Estados.Terminado;
        colaLista.poll();
        listaProcesosTerminados.add(proceso);
    }
}


