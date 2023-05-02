import java.util.List;
public class Proceso implements Runnable{
    public long tiempoEjecucion;
    public String id;
    public int prioridad;
    public List<IRecurso> recursosUsados;
    public Estados estado;
    public Proceso(String id, long tiempoEjecucion,
                   int prioridad, List<IRecurso> recursosUsados, Estados estado) {
        this.id = id;
        this.tiempoEjecucion = tiempoEjecucion;
        this.prioridad = prioridad;
        this.recursosUsados = recursosUsados;
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

    public enum Estados{
        Nuevo,
        Listo,
        Correr,
        Bloquear,
        Finalizar,
        ListoParaSuspender,
        SuspenderEspera
    }
}








