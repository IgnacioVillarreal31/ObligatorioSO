import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

public class Aeropuerto {
    protected Pista pista1;
    protected Pista pista2;
    protected Pista pistaActiva;
    protected final Semaphore permisoUsarPista = new Semaphore(15);

    public PriorityQueue<Avion> cola;

    public Aeropuerto() {
        //fair le da el lock al thread con mayor tiempo (Funciona de forma fifo)
        pista1 = new Pista(new Semaphore(1, true), "01-19");
        pista2 = new Pista(new Semaphore(1, true), "06-24");
        pistaActiva = pista1;
        cola = new PriorityQueue<>(15, Avion::compareTo);
    }

    public void insertarEnCola(Avion avion) {
        this.cola.offer(avion);
    }

    public void PedirPermisoAterrizar() {


    }

    public void aterrizar() {

    }

    public void despegar() {

    }

    public void run() {
        //recorrer cola
        while (!cola.isEmpty()) {
            Avion avion = cola.poll();
            this.estados(avion);
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
