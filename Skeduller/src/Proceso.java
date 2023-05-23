import java.util.List;
import java.util.PriorityQueue;
public class Proceso implements Runnable, Comparable<Proceso>{
    public long tiempoEjecucion;
    public String id;
    public int prioridad;
    public List<IRecurso> recursosUsados;
    public Estados estado;
    public Proceso(String id, long tiempoEjecucion, int prioridad, List<IRecurso> recursosUsados, Estados estado) {
        this.id = id;
        this.tiempoEjecucion = tiempoEjecucion;
        this.prioridad = prioridad;
        this.recursosUsados = recursosUsados;
        this.estado = estado;
    }

    public Estados getEstado(){
        return this.estado;
    }

    public void setEstado(Estados estado){
        this.estado = estado;
    }
    public void run() {
        System.out.println("Ejecutando " + id + "...");
        try {
            Thread.sleep(tiempoEjecucion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(id + " terminado.");
    }

    public void runMax() {
        System.out.println("Ejecutando " + id + "...");
        try {
            long currentTime = System.currentTimeMillis();
            Thread.sleep(currentTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(id + " terminado.");
    }

    public int compareTo(Proceso otroProceso){
        return Integer.compare(this.prioridad, otroProceso.prioridad);
    }

    public enum Estados{
        Listo,
        Ejecucion,
        Bloqueado,
        Terminado,
        SuspendidoListo,
        SuspendidoBloqueado
    }
}








