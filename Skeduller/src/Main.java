import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IRecurso r1 = new Impresora();
        List<IRecurso> recursos = new ArrayList<>();
        recursos.add(r1);

        Proceso p1 = new Proceso("1111",5000,2,recursos, Proceso.Estados.Listo);
        Proceso p2 = new Proceso("1112",168363411100000000L,3,recursos, Proceso.Estados.Listo);
        Proceso p3 = new Proceso("1113",3320,1,recursos, Proceso.Estados.Listo);


        Scheduller scheduller = new Scheduller(recursos);
        scheduller.agregarProceso(p1);
        scheduller.agregarProceso(p3);
        scheduller.agregarProceso(p2);
        scheduller.ejecutarProcesos();
    }
}