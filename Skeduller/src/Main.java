import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //Se crean los recursos, en este caso, impresoras
        IRecurso r1 = new Impresora();
        IRecurso r2 = new Impresora();
        //El segundo recurso está roto
        r2.setEstaRoto(true);
        //Se agregan los recursos disponibles a una lista
        List<IRecurso> recursosDisponibles = new ArrayList<>();
        recursosDisponibles.add(r1);
        //Se agregan los recursos suspendidos a una lista
        List<IRecurso> recursosSuspendido = new ArrayList<>();
        recursosSuspendido.add(r2);

        //Se crean los procesos
        Proceso p1 = new Proceso("1111",5000,2,recursosDisponibles, Proceso.Estados.Listo,"src/Logs.txt");
        Proceso p2 = new Proceso("1112",3000,3,recursosDisponibles, Proceso.Estados.Listo,"src/Logs.txt");
        Proceso p3 = new Proceso("1113",3320,1, recursosSuspendido, Proceso.Estados.Listo,"src/Logs.txt");
        Proceso p4 = new Proceso("1114",3320,1, recursosSuspendido, Proceso.Estados.Listo,"src/Logs.txt");
        Proceso p5 = new Proceso("1115",3320,1, recursosSuspendido, Proceso.Estados.Bloqueado,"src/Logs.txt");

        //Se inicializan los procesos, el p3 no se debería correr debido a que el recurso que necesita está roto
        Scheduller scheduller = new Scheduller(recursosDisponibles,"src/Logs.txt");
        scheduller.agregarProceso(p1);
        scheduller.agregarProceso(p3);
        scheduller.agregarProceso(p2);
        scheduller.agregarProceso(p5);
        scheduller.ejecutarProcesos();

        //El recurso necesario para ejecutar p3 se arreglo, por lo que p3 se debería ejecutar luego de p4, que también debería ejecutarse ya que se arregló su recurso
        r2.setEstaRoto(false);
        scheduller.agregarProceso(p4);
        scheduller.ejecutarProcesos();
    }
}