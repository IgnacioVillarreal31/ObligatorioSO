import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

public class Aeropuerto implements Runnable {
    protected Pista pista1;
    protected Pista pista2;
    protected Pista pistaActiva;
    protected final Semaphore permisoUsarPista = new Semaphore(15);

    protected PriorityBlockingQueue<Avion> aterrizar;
    protected ArrayList<Avion> aviones;

    public Aeropuerto() {
        //fair le da el lock al thread con mayor tiempo (Funciona de forma FIFO)
        pista1 = new Pista(new Semaphore(1, true), "01-19");
        pista2 = new Pista(new Semaphore(1, true), "06-24");

        aterrizar = new PriorityBlockingQueue<>(15, Avion::compareTo);
        aviones = new ArrayList<Avion>();
        this.elegirPistaActiva();
    }

    public void agregar(Avion avion) {
        this.aviones.add(avion);
    }

    private void elegirPistaActiva() {
        pistaActiva = pista1;
    }

    public void pedirPermisoAterrizar(Avion avion) {

        if (!aterrizar.contains(avion)) {
            aterrizar.add(avion);
        }

    }

    public void aterrizar() {

    }

    public void despegar() {

    }

    public void run() {
        // inicializar todos los aviones
        for (Avion avion : aviones) {
            Thread t1 = new Thread(avion);
            t1.start();
        }

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
        }

    }

    private void estados(Avion avion) {
        try {
            //System.out.println("Prioridad " + avion.nombre + " :" + avion.prioridad);

            switch (avion.estado) {
                case Volando:
                    //aterrizar el avion y cambiar estado a aterrizando
                    avion.aterrizar();
                    break;
                case Aterrizando:
                    //taxear a porton y cambiar estado a estacionando
                    avion.taxear(0, 0);
                    avion.estado = Avion.Estados.Estacionando;
                    break;
                case Despegando:
                    //despegar el avion y cambiar el estado a volando?
                    avion.despegar();
                    break;
                case Estacionando:
                    //estacionar y cambiar el estado a estacionado
                    avion.estacionar();
                    break;
                case Estacionado:
                    //dejar de actualizar avion y quizas borrarlo de la lista de aviones? o ponerle el estado EnPiso

                    break;
                case EnPiso:
                    //taxear a cabecera de pista, y cambiar estado a despegando
                    avion.taxear(0, 0);
                    avion.estado = Avion.Estados.Despegando;
                    break;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
