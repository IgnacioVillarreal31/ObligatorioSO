import java.util.List;
public class Proceso implements Runnable{
    public String nombre;
    public int tiempoEjecucion;
    public long scheduledTime;
    public String id;
    public int prioridad;
    public List<IRecurso> recursos;
    public Estados estado;
    public enum Estados{
        Nuevo,
        Listo,
        Correr,
        Bloquear,
        Finalizar,
        ListoParaSuspender,
        SuspenderEspera
    }
    public Proceso(String nombre, int tiempoEjecucion, long scheduledTime,
                   int prioridad, List<IRecurso> recursos, Estados estado) {
        this.nombre = nombre;
        this.tiempoEjecucion = tiempoEjecucion;
        this.scheduledTime = scheduledTime;
        this.prioridad = prioridad;
        this.recursos = recursos;
        this.estado = estado;

    }
    public void run() {
        System.out.println("Ejecutando " + nombre + "...");
        try {
            Thread.sleep(tiempoEjecucion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(nombre + " terminado.");
    }


}








