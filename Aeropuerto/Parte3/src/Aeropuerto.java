import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Aeropuerto implements Runnable {
    protected Pista pista0119;
    protected Pista pista0624;
    protected Pista pistaActiva;
    protected final Semaphore permisoUsarPista = new Semaphore(15);
    public PriorityBlockingQueue<Avion> aterrizar;
    protected ArrayList<Avion> aviones;
    protected int direccionViento;

    public Aeropuerto() {
        //fair le da el lock al thread con mayor tiempo (Funciona de forma FIFO)
        pista0119 = new Pista(new Semaphore(1, true), "01-19");
        pista0624 = new Pista(new Semaphore(1, true), "06-24");
        direccionViento = ThreadLocalRandom.current().nextInt(359); // elige la direccion del viento entre 0 y 359
        aterrizar = new PriorityBlockingQueue<>(15, Avion::compareTo);
        aviones = new ArrayList<Avion>();
        this.elegirPistaActiva();
    }

    public void agregar(Avion avion) {
        this.aviones.add(avion);
    }

    private void elegirPistaActiva() {

        if (this.getDireccionViento() >= 30 && this.getDireccionViento() <= 119) {
            //pista 24
            //this.pistaActiva = pista0624;
            this.setPistaActiva("24");
        } else if (this.getDireccionViento() >= 120 && this.getDireccionViento() <= 209) {
            //pista 19
            //this.pistaActiva = pista0119;
            this.setPistaActiva("19");
        } else if (this.getDireccionViento() >= 210 && this.getDireccionViento() <= 299) {
            //pista 06
            //this.pistaActiva = pista0624;
            this.setPistaActiva("06");
        } else {
            //pista 01
            //this.pistaActiva = pista0119;
            this.setPistaActiva("01");
        }

    }

    public synchronized Pista getPistaActiva() {
        return this.pistaActiva;
    }

    public synchronized void setPistaActiva(String pista) {
        if (pista.equals("24") || pista.equals("06")) {
            this.pistaActiva = pista0624;
        } else if (pista.equals("01") || pista.equals("19")) {
            this.pistaActiva = pista0119;
        }
    }

    public synchronized int getDireccionViento() {
        return this.direccionViento;
    }

    public synchronized void setDireccionViento(int direccion) {
        this.direccionViento = direccion;
    }

    public void pedirPermisoAterrizar(Avion avion) {

        if (!aterrizar.contains(avion)) {
            aterrizar.add(avion);
        }

    }


    public void run() {
        // inicializar todos los aviones
        for (Avion avion : aviones) {
            Thread t1 = new Thread(avion);
            t1.setPriority(Thread.NORM_PRIORITY);
            t1.start();
        }

        //parte grafica
        Graficos graficos = new Graficos(this);
        Thread g = new Thread(graficos);
        g.setPriority(Thread.NORM_PRIORITY);
        g.start();

        //recorrer colas de prioridad y ver quienes quieren aterrizar y eso
        Thread recorrer = new Thread(new Recorrer(this));
        recorrer.setPriority(Thread.NORM_PRIORITY);
        recorrer.start();


/*
        while (true) {
            //crear thread recorrer pbq y crear otro thread para actualizar los graficos
            while (!aterrizar.isEmpty()) {
                Avion avion = aterrizar.poll();
                try {
                    avion.aterrizar();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }*/

    }

}
