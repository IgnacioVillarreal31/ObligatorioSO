import java.security.Timestamp;

public class Avion implements Comparable<Avion> {

    protected boolean tienePrioridad = false;
    private boolean tienePermisoUsarPista = false;
    protected int prioridad;
    protected String nombre;

    protected Estados estado;
    protected int nivelCombustible;
    //dos semaforos, uno para pedir permiso para aterizar y otro para usar la pista,
    // resuelve el problema de aterrizar aviones con prioridad
    private Aeropuerto aeropuerto;

    protected long timestamp;

    public Avion(Aeropuerto aeropuerto, int prioridad, String nombre, int nivelCombustible, Estados estado) {
        this.aeropuerto = aeropuerto;
        this.nivelCombustible = nivelCombustible;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.timestamp = System.nanoTime();
        this.estado = estado;

    }

    public void Despegar() throws InterruptedException {
        //taxear a donde tenga que ir
        //pedir permiso para usar la pista y despues usar la pista
        aeropuerto.permisoUsarPista.acquire();
        aeropuerto.pistaActiva.usar.acquire();
        //despegar
        System.out.println(this.nombre + " esta despegando en la pista " + aeropuerto.pistaActiva);
        Thread.sleep(1000);
        aeropuerto.pistaActiva.usar.release();
        aeropuerto.permisoUsarPista.release();
    }

    public void aterrizar() throws InterruptedException {
        //pedir permiso para usar la pista, si no tiene prioridad, y despues usar la pista
        /*
        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.acquire();
            System.out.println(this.nombre + " pidio permiso para aterrizar");
            Thread.sleep(1000);
            this.tienePermisoUsarPista = true;
        }
        */

        aeropuerto.pistaActiva.usar.acquire();
        System.out.println(this.nombre + " va a usar la pista para aterrizar");
        Thread.sleep(1000);
        //aterizar
        aeropuerto.pistaActiva.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista");
        this.estado = Estados.Aterrizando;
        Thread.sleep(1000);
        /*
        if (this.tienePermisoUsarPista) {
            aeropuerto.permisoUsarPista.release();
            System.out.println(this.nombre + " devolvio el permiso para aterrizar");
            Thread.sleep(1000);
        }*/
        //taxear a donde tenga que ir
    }

    public void pedirPrioridad() {
        this.tienePrioridad = true;
        Avion avion = this;
        aeropuerto.cola.remove(this);
        avion.timestamp = System.nanoTime();
        avion.prioridad = 0;
        aeropuerto.cola.offer(avion);
    }

    @Override
    public int compareTo(Avion avion) {

        if (avion.prioridad > this.prioridad) return -1;
        else if (avion.prioridad < this.prioridad) return 1;
        else {
            //Si tiene la misma prioridad, que ponga primero el que fue creado antes
            if (avion.timestamp < this.timestamp) return 1;
            return -1;

        }
    }

    public void cruzar() {

    }

    public void taxear(int x, int y) {
        //ver de ir a un porton aleatorio y como cruzar la pista, entre otros.
    }

    public void despegar() {
        this.estado = Estados.Volando;
    }

    public void estacionar() {
        //ver de ir a un porton aleatorio y como cruzar la pista, entre otros.
        this.estado = Estados.Estacionado;
    }

    public enum Estados {
        Volando, Aterrizando, Despegando, Estacionando, Estacionado, EnPiso
    }
}


