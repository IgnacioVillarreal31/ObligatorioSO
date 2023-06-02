import java.awt.*;
import java.security.Timestamp;

public class Avion implements Comparable<Avion>, Runnable {

    final int SQUARE_SIZE = 25;
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

    //para los graficos
    private int x;
    private int y;
    private int dx = 1;
    private int dy = 1;
    private int targetX;
    private int targetY;
    private boolean moving;

    private Posicion[] posiciones = new Posicion[10];

    private int posicion = 0;

    public Avion(Aeropuerto aeropuerto, int prioridad, String nombre, int nivelCombustible, Estados estado, int x, int y) {
        this.aeropuerto = aeropuerto;
        this.nivelCombustible = nivelCombustible;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.timestamp = System.nanoTime();
        this.estado = estado;


        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.moving = false;
        posiciones[0] = new Posicion(597, 153);
        posiciones[1] = new Posicion(601, 494);
        posiciones[2] = new Posicion(600, 550);
        posiciones[3] = new Posicion(457, 591);
        posiciones[4] = new Posicion(349, 582);
        posiciones[5] = new Posicion(247, 304);
        posiciones[6] = new Posicion(252, 167);
        posiciones[7] = new Posicion(305, 88);
        posiciones[8] = new Posicion(430, 56);
        posiciones[9] = new Posicion(529, 54);


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

        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.acquire();
            System.out.println(this.nombre + " pidio permiso para aterrizar");
            Thread.sleep(1000);
            this.tienePermisoUsarPista = true;
        }

        aeropuerto.pistaActiva.usar.acquire();
        System.out.println(this.nombre + " va a usar la pista para aterrizar");
        Thread.sleep(1000);
        //aterizar
        aeropuerto.pistaActiva.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista");
        this.estado = Estados.Aterrizando;
        Thread.sleep(1000);

        if (this.tienePermisoUsarPista) {
            aeropuerto.permisoUsarPista.release();
            System.out.println(this.nombre + " devolvio el permiso para aterrizar");
            Thread.sleep(1000);
        }
        //taxear a donde tenga que ir
    }

    public void cambiarPrioridad(int prioridad) {
        //cambiar prioridad
        Avion avion = this;
        aeropuerto.aterrizar.remove(this);
        avion.timestamp = System.nanoTime();
        avion.prioridad = prioridad;
        aeropuerto.aterrizar.offer(avion);
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

    @Override
    public void run() {
        //cambiar de estados viendo si ya esta donde debe estar o no.

        try {
            //System.out.println("Prioridad " + avion.nombre + " :" + avion.prioridad);

            switch (this.estado) {
                case Volando:
                    //aterrizar el avion y cambiar estado a aterrizando
                    //this.aterrizar();
                    aeropuerto.pedirPermisoAterrizar(this);

                    break;
                case Aterrizando:
                    //taxear a porton y cambiar estado a estacionando
                    this.taxear(0, 0);
                    this.estado = Avion.Estados.Estacionando;
                    break;
                case Despegando:
                    //despegar el avion y cambiar el estado a volando?
                    this.despegar();
                    break;
                case Estacionando:
                    //estacionar y cambiar el estado a estacionado
                    this.estacionar();
                    break;
                case Estacionado:
                    //dejar de actualizar avion y quizas borrarlo de la lista de aviones? o ponerle el estado EnPiso

                    break;
                case EnPiso:
                    //taxear a cabecera de pista, y cambiar estado a despegando
                    this.taxear(0, 0);
                    this.estado = Avion.Estados.Despegando;
                    break;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void move() {
        if (moving) {
            if (x < targetX) {
                x += 1;
            } else if (x > targetX) {
                x -= 1;
            }
            if (y < targetY) {
                y += 1;
            } else if (y > targetY) {
                y -= 1;
            }
            if (x == targetX && y == targetY) {
                moving = false;
            }
        }
    }

    public void moveTo(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.moving = true;
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + SQUARE_SIZE && py >= y && py <= y + SQUARE_SIZE;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
    }

    public void nextPosition() {
        if (!moving) {
            moveTo(posiciones[posicion].x, posiciones[posicion].y);
            posicion = (posicion + 1) % 10;
        }
    }


    public enum Estados {
        Volando, Aterrizando, Despegando, Estacionando, Estacionado, EnPiso
    }
}


