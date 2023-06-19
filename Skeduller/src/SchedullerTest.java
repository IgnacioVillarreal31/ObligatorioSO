import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedullerTest {

    @org.junit.jupiter.api.Test
    void ejecutarProcesosMismaPrioridad() throws InterruptedException {
        IRecurso r1 = new Impresora();
        List<IRecurso> recursosDisponibles = new ArrayList<>();
        recursosDisponibles.add(r1);

        Proceso p1 = new Proceso("1111",5000,1,recursosDisponibles, Proceso.Estados.Listo,"src/Logs2.txt");
        Proceso p2 = new Proceso("1112",3000,1,recursosDisponibles, Proceso.Estados.Listo,"src/Logs2.txt");
        Proceso p3 = new Proceso("1113",3320,1, recursosDisponibles, Proceso.Estados.Listo,"src/Logs2.txt");


        Scheduller scheduller = new Scheduller(recursosDisponibles,"src/Logs2.txt");
        scheduller.agregarProceso(p1);
        scheduller.agregarProceso(p3);
        scheduller.agregarProceso(p2);
        scheduller.ejecutarProcesos();
        String stringEsperado = ManejadorDeArchivos.leerArchivo("src/Logs2Esperado.txt");
        String stringResultado = ManejadorDeArchivos.leerArchivo("src/Logs2.txt");
        assertNotEquals(stringResultado,stringEsperado);
    }

    @org.junit.jupiter.api.Test
    void ejecutarProcesosDiferentePrioridad() throws InterruptedException {
        IRecurso r1 = new Impresora();
        IRecurso r2 = new Impresora();
        r2.setEstaRoto(true);
        List<IRecurso> recursosDisponibles = new ArrayList<>();
        recursosDisponibles.add(r1);
        List<IRecurso> recursosSuspendido = new ArrayList<>();
        recursosSuspendido.add(r2);

        Proceso p1 = new Proceso("1111",5000,2,recursosDisponibles, Proceso.Estados.Listo,"src/Logs1.txt");
        Proceso p2 = new Proceso("1112",3000,3,recursosDisponibles, Proceso.Estados.Listo,"src/Logs1.txt");
        Proceso p3 = new Proceso("1113",3320,1, recursosSuspendido, Proceso.Estados.Listo,"src/Logs1.txt");
        Proceso p4 = new Proceso("1114",3320,1, recursosSuspendido, Proceso.Estados.Listo,"src/Logs1.txt");
        Proceso p5 = new Proceso("1115",3320,1, recursosSuspendido, Proceso.Estados.Bloqueado,"src/Logs1.txt");

        Scheduller scheduller = new Scheduller(recursosDisponibles,"src/Logs1.txt");
        scheduller.agregarProceso(p1);
        scheduller.agregarProceso(p3);
        scheduller.agregarProceso(p2);
        scheduller.agregarProceso(p5);
        scheduller.ejecutarProcesos();

        r2.setEstaRoto(false);
        scheduller.agregarProceso(p4);
        scheduller.ejecutarProcesos();
        String stringEsperado = ManejadorDeArchivos.leerArchivo("src/Logs1Esperado.txt");
        String stringResultado = ManejadorDeArchivos.leerArchivo("src/Logs1.txt");
        assertNotEquals(stringResultado,stringEsperado);
    }
}