import java.util.List;
import java.util.PriorityQueue;
public class Proceso implements Runnable, Comparable<Proceso>{
    //Creación de variables
    public long tiempoEjecucion;
    public String id;
    public int prioridad;
    public List<IRecurso> recursosUsados;
    public Estados estado;
    public String ruta;

    //Inicialización de variables
    public Proceso(String id, long tiempoEjecucion, int prioridad, List<IRecurso> recursosUsados, Estados estado, String ruta) {
        this.id = id;
        this.tiempoEjecucion = tiempoEjecucion;
        this.prioridad = prioridad;
        this.recursosUsados = recursosUsados;
        this.estado = estado;
        this.ruta = ruta;
    }

    public Estados getEstado(){
        return this.estado;
    }

    public void setEstado(Estados estado){
        this.estado = estado;
    }

    //Ejecuta el proceso si su tiempo es menor o igual al del TimeOut
    public void run() {
        ManejadorDeArchivos.escribirArchivo(ruta, "Ejecutando " + id + "...");
        System.out.println("Ejecutando " + id + "...");
        try {
            Thread.sleep(tiempoEjecucion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + id + " se termino...");
        System.out.println(id + " terminado.");
    }

    //Ejecuta un proceso si su tiempo es mayor al del TimeOut y tiene que ejecutarse dos o más veces
    public void runMax() {
        ManejadorDeArchivos.escribirArchivo(ruta, "Ejecutando " + id + "...");
        System.out.println("Ejecutando " + id + "...");
        try {
            long currentTime = System.currentTimeMillis();
            Thread.sleep(currentTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ManejadorDeArchivos.escribirArchivo(ruta, "El proceso " + id + " se termino...");
        System.out.println(id + " terminado.");
    }

    //M+etodo diseñado para generar una comparación entre procesos teniendo en cuenta su prioridad
    public int compareTo(Proceso otroProceso){
        return Integer.compare(this.prioridad, otroProceso.prioridad);
    }

    //Posibles estados de un proceso
    public enum Estados{
        Listo,
        Ejecucion,
        Bloqueado,
        Terminado,
        SuspendidoListo,
        SuspendidoBloqueado
    }
}
