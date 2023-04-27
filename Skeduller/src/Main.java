import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IRecurso r1 = new Impresora();

        List<IRecurso> recursos = new ArrayList<>();
        recursos.add(r1);

        Proceso p1 = new Proceso("1111",1,1,recursos, Proceso.Estados.Nuevo);
        Proceso p2 = new Proceso("1112",212,2,recursos, Proceso.Estados.Nuevo);
        Proceso p3 = new Proceso("1113",33,3,recursos, Proceso.Estados.Nuevo);

        Scheduller scheduller = new Scheduller(recursos);
        scheduller.agregarProceso(p1);
        scheduller.agregarProceso(p2);
        scheduller.agregarProceso(p3);
        scheduller.ejecutarProcesos();
    }
}