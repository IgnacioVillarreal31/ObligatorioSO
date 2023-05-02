import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IRecurso r1 = new Impresora();
        List<IRecurso> recursos = new ArrayList<>();
        recursos.add(r1);

        PriorityQueue<Proceso> colaPrioridades = new PriorityQueue<>();
        colaPrioridades.offer(new Proceso("1", 1, 1, recursos, Proceso.Estados.Nuevo));
        colaPrioridades.offer(new Proceso("2", 212, 2, recursos, Proceso.Estados.Nuevo));
        colaPrioridades.offer(new Proceso("3", 33, 3, recursos, Proceso.Estados.Nuevo));

        while (!colaPrioridades.isEmpty()) {
            Proceso proceso = colaPrioridades.poll();
            System.out.println("Id: " + proceso.id + ", Prioridad: " + proceso.prioridad);
        }
/*        Proceso p1 = new Proceso("1111",1,1,recursos, Proceso.Estados.Nuevo);
        Proceso p2 = new Proceso("1112",212,2,recursos, Proceso.Estados.Nuevo);
        Proceso p3 = new Proceso("1113",33,3,recursos, Proceso.Estados.Nuevo);
*/

        Scheduller scheduller = new Scheduller(recursos);
/*        scheduller.agregarProceso(p1);
        scheduller.agregarProceso(p2);
        scheduller.agregarProceso(p3);*/
        scheduller.ejecutarProcesos();
    }
}