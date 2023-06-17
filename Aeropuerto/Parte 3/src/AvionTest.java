import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Semaphore;

import static org.junit.Assert.*;

public class AvionTest {

    @Test
    public void TestAterrizar() {
        ManejadorArchivosGenerico.lineas = new StringBuffer();
        int numAviones = 5;
        Semaphore rec = new Semaphore(1); //semaforo de recorrer para que solo permita que uno aterrice por vez
        Aeropuerto aeropuerto = new Aeropuerto(rec);
        aeropuerto.setPistaActiva("19");
        Model model = new Model(aeropuerto);
        View view = new View(model);

        for (int i = 0; i < numAviones; i++) {
            Avion b = new Avion("Avion " + i, Avion.Estados.Esperando, aeropuerto); //construct  a ball
            model.addBall(b);    //add it to the model
            b.registerObserver(view.getObserver());  //register view as an observer to it
            new AvionAnimator(b, model.getWidth(), model.getHeight(), i, rec); //start a thread to update it
        }
        Recorrer r = new Recorrer(aeropuerto, rec);
        r.parador = true;
        AvionAnimator.parador = true;
        r.start();
        try {
            Thread.sleep(50000);
            r.parador = false;
            AvionAnimator.parador = false;
            String linea[] = ManejadorArchivosGenerico.lineas.toString().split("\\.");
            assertEquals(linea[0], "Avion 0 va a usar la pista 19 para aterrizar");
            assertEquals(linea[1], "Avion 0 aterrizó y devolvio el uso de la pista 19");
            assertEquals(linea[2], "Avion 1 va a usar la pista 19 para aterrizar");
            assertEquals(linea[3], "Avion 1 aterrizó y devolvio el uso de la pista 19");
            assertEquals(linea[4], "Avion 2 va a usar la pista 19 para aterrizar");
            assertEquals(linea[5], "Avion 2 aterrizó y devolvio el uso de la pista 19");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestAterrizarConPrioridad() {
        ManejadorArchivosGenerico.lineas = new StringBuffer();
        int numAviones = 5;
        Semaphore rec = new Semaphore(1); //semaforo de recorrer para que solo permita que uno aterrice por vez
        Aeropuerto aeropuerto = new Aeropuerto(rec);
        aeropuerto.setPistaActiva("19");
        Model model = new Model(aeropuerto);
        View view = new View(model);

        for (int i = 0; i < numAviones; i++) {
            Avion b = new Avion("Avion " + i, Avion.Estados.Esperando, aeropuerto); //construct  a ball
            model.addBall(b);    //add it to the model
            b.registerObserver(view.getObserver());  //register view as an observer to it
            new AvionAnimator(b, model.getWidth(), model.getHeight(), i, rec); //start a thread to update it
        }
        Avion b = new Avion("Avion 5", Avion.Estados.Esperando, aeropuerto); //construct  a ball
        b.setPrioridad(0);
        model.addBall(b);    //add it to the model
        b.registerObserver(view.getObserver());  //register view as an observer to it
        new AvionAnimator(b, model.getWidth(), model.getHeight(), 5, rec); //start a thread to update it
        Recorrer r = new Recorrer(aeropuerto, rec);
        r.parador = true;
        AvionAnimator.parador = true;
        r.start();
        try {
            Thread.sleep(40000);
            r.parador = false;
            AvionAnimator.parador = false;
            String linea[] = ManejadorArchivosGenerico.lineas.toString().split("\\.");
            assertEquals(linea[0], "Avion 5 va a usar la pista 19 para aterrizar");
            assertEquals(linea[1], "Avion 5 aterrizó y devolvio el uso de la pista 19");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}