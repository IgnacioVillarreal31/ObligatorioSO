import javax.swing.*;
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

    private int posicion = 0;

    private final int OFFSET_X = 20;

    private final int OFFSET_Y = 20;

    public Panel panel;

    public Posiciones posiciones;

    public Posicion siguientePosicion;

    public Avion(Aeropuerto aeropuerto, int prioridad, String nombre, int nivelCombustible, Estados estado, int x, int y, ImageIcon icono) {
        this.aeropuerto = aeropuerto;
        this.nivelCombustible = nivelCombustible;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.timestamp = System.nanoTime();
        this.estado = estado;

        this.panel = new Panel(nombre, estado.toString(), icono);

        this.posiciones = new Posiciones();

        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.moving = false;

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
            this.panel.setLocation(x - OFFSET_X, y - OFFSET_Y);
        }
    }

    public void moveTo(int targetX, int targetY) {
        this.panel.rotarIcono(calcRotationAngleInDegrees(new Posicion(x, y, false), new Posicion(targetX, targetY, false)));
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
        //poner la maquina de estados aca?
        if (this.estado == Estados.Volando) {
            this.siguientePosicion = posiciones.esperar.get(posiciones.posicionEsperar);
            posiciones.posicionEsperar = (posiciones.posicionEsperar + 1) % posiciones.esperar.size();
        } else if (this.estado == Estados.Aterrizando) {
            //crear un estado para cada pista
            //this.siguientePosicion = posiciones.aterrizar01.get();
        }

        if (!moving && this.siguientePosicion.permiso) {

            moveTo(siguientePosicion.x, siguientePosicion.y);
            posicion = (posicion + 1) % 10;

        }
    }

    private double calcRotationAngleInDegrees(Posicion centerPt, Posicion targetPt) {
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.
        // NOTE: By preserving Y and X param order to atan2,  we are expecting
        // a CLOCKWISE angle direction.
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        // rotate the theta angle clockwise by 90 degrees
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.
        // subtracting would rotate it counter-clockwise
        theta += Math.PI / 2.0;

        // convert from radians to degrees
        // this will give you an angle from [0->270],[-180,0]
        double angle = Math.toDegrees(theta);

        // convert to positive range [0-360)
        // since we want to prevent negative angles, adjust them now.
        // we can assume that atan2 will not return a negative value
        // greater than one partial rotation
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }


    public enum Estados {
        Volando, Aterrizando, Despegando, Estacionando, Estacionado, EnPiso

    }
}


