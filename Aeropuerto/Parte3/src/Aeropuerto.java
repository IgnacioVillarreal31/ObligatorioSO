import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Aeropuerto implements Runnable {
    protected Pista pista0119;
    protected Pista pista0624;
    protected Pista pistaActiva;
    protected final Semaphore permisoUsarPista = new Semaphore(1);
    public PriorityBlockingQueue<Avion> aterrizar;
    protected HashMap<String, Avion> aviones;
    protected int direccionViento;

    protected String numeroPistaActiva;

    public Aeropuerto() {
        //fair le da el lock al thread con mayor tiempo (Funciona de forma FIFO)
        pista0119 = new Pista(new Semaphore(1, true), "01-19");
        pista0624 = new Pista(new Semaphore(1, true), "06-24");
        direccionViento = ThreadLocalRandom.current().nextInt(359); // elige la direccion del viento entre 0 y 359
        aterrizar = new PriorityBlockingQueue<>(15, Avion::compareTo);
        aviones = new HashMap<String, Avion>();
        this.elegirPistaActiva();
    }

    public void agregar(Avion avion) {
        this.aviones.put(avion.nombre, avion);
    }

    private void elegirPistaActiva() {
        int direccionViento = this.getDireccionViento();

        if (direccionViento >= 30 && direccionViento <= 119) {
            //pista 24
            //this.pistaActiva = pista0624;
            this.setPistaActiva("24");
            this.setNumeroPistaActiva("24");
        } else if (direccionViento >= 120 && direccionViento <= 209) {
            //pista 19
            //this.pistaActiva = pista0119;
            this.setPistaActiva("19");
            this.setNumeroPistaActiva("19");
        } else if (direccionViento >= 210 && direccionViento <= 299) {
            //pista 06
            //this.pistaActiva = pista0624;
            this.setPistaActiva("06");
            this.setNumeroPistaActiva("06");
        } else {
            //pista 01
            //this.pistaActiva = pista0119;
            this.setPistaActiva("01");
            this.setNumeroPistaActiva("01");
        }

    }

    private synchronized void setNumeroPistaActiva(String pistaActiva) {
        this.numeroPistaActiva = pistaActiva;
    }

    public synchronized Object[] getPistaActiva() {
        Object[] arr = new Object[]{this.pistaActiva, this.getNumeroPistaActiva()};
        return arr;
    }

    public synchronized void setPistaActiva(String pista) {
        if (pista.equals("24") || pista.equals("06")) {
            this.pistaActiva = pista0624;
        } else if (pista.equals("01") || pista.equals("19")) {
            this.pistaActiva = pista0119;
        }
        this.setNumeroPistaActiva(pista);
    }

    public synchronized String getNumeroPistaActiva() {
        return this.numeroPistaActiva;
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
        Recorrer r = new Recorrer(this);
        for (Avion avion : aviones.values()) {
            avion.setRecorrer(r);
            Thread t1 = new Thread(avion);
            t1.setPriority(Thread.NORM_PRIORITY);
            t1.start();
        }

        //parte grafica
        Graficos graficos = new Graficos(this);
        Thread g = new Thread(graficos);
        g.setPriority(Thread.NORM_PRIORITY);


        //recorrer colas de prioridad y ver quienes quieren aterrizar y eso
        Thread recorrer = new Thread(r);
        recorrer.setPriority(Thread.NORM_PRIORITY);
        g.start();
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
