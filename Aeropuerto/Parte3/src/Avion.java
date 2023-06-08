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

    private final int OFFSET_X = 15;

    private final int OFFSET_Y = 15;

    public Panel panel;

    public Posiciones posiciones;

    public Posicion siguientePosicion;

    public Pista pistaUsada;

    public String numeroPistaUsada;

    public Avion(Aeropuerto aeropuerto, int prioridad, String nombre, int nivelCombustible, Estados estado, int x, int y, ImageIcon icono) {
        this.aeropuerto = aeropuerto;
        this.nivelCombustible = nivelCombustible;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.timestamp = System.nanoTime();
        this.estado = estado;

        this.panel = new Panel(nombre, estado.toString(), icono);

        this.posiciones = new Posiciones();
        this.posicion = 0;
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
        }

        Object[] pista = aeropuerto.getPistaActiva();
        pistaUsada = (Pista) pista[0];
        pistaUsada.usar.acquire();
        this.tienePermisoUsarPista = true;
        //ver la pista activa
        this.numeroPistaUsada = pista[1].toString();
        if (this.numeroPistaUsada == "01") {
            this.estado = Estados.Aterrizando01;
            //this.aterrizar01();

        } else if (this.numeroPistaUsada == "19") {
            this.estado = Estados.Aterrizando19;
            //this.aterrizar19();

        } else if (this.numeroPistaUsada == "06") {
            this.estado = Estados.Aterrizando06;
            //this.aterrizar06();
        } else {
            //pista 24
            this.estado = Estados.Aterrizando24;
            //this.aterrizar24();
        }
        System.out.println(this.nombre + " va a usar la pista " + this.numeroPistaUsada + " para aterrizar.");
        //actualiza el estado en la parte grafica
        this.panel.cambiarEstado(this.estado.toString());
    }


    public void aterrizar01() throws InterruptedException {
        //aterizar
        pistaUsada.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista 01.");
        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.release();
        }
        this.tienePermisoUsarPista = false;

    }

    public void taxear01Porton() {
        //salgo de la pista
        pistaUsada.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista 01.");
        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.release();
        }
        this.tienePermisoUsarPista = false;
    }

    public void taxear06Porton() {
        //salgo de la pista
        pistaUsada.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista 06.");
        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.release();
        }
        this.tienePermisoUsarPista = false;
    }


    public void taxear19Porton() {
        //salgo de la pista
        pistaUsada.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista 19.");
        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.release();
        }
        this.tienePermisoUsarPista = false;
    }

    public void taxear24Porton() {
        //salgo de la pista
        pistaUsada.usar.release();
        System.out.println(this.nombre + " aterrizó y devolvio el uso de la pista 24.");
        if (this.prioridad != 0) {
            aeropuerto.permisoUsarPista.release();
        }
        this.tienePermisoUsarPista = false;
    }

    public void aterrizar06() throws InterruptedException {


    }

    public void aterrizar19() throws InterruptedException {


    }

    public void aterrizar24() throws InterruptedException {


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

    }

    public void estacionar() {
        //ver de ir a un porton aleatorio y como cruzar la pista, entre otros.

    }

    @Override
    public void run() {
        //cambiar de estados viendo si ya esta donde debe estar o no.

        try {
            //System.out.println("Prioridad " + avion.nombre + " :" + avion.prioridad);

            switch (this.estado) {
                case Esperando:
                    //aterrizar el avion y cambiar estado a aterrizando
                    //this.aterrizar();
                    aeropuerto.pedirPermisoAterrizar(this);
                    break;
                case Aterrizando01:
                    break;
                case Aterrizando06:
                    break;
                case Aterrizando19:
                    break;
                case Aterrizando24:
                    break;
                case Despegando01:
                    break;
                case Despegando06:
                    break;
                case Despegando19:
                    break;
                case Despegando24:
                    break;
                case Taxeando01Porton:
                    break;
                case Taxeando06Porton:
                    break;
                case Taxeando19Porton:
                    break;
                case Taxeando24Porton:
                    break;
                case TaxeandoPorton01:
                    break;
                case TaxeandoPorton06:
                    break;
                case TaxeandoPorton19:
                    break;
                case TaxeandoPorton24:
                    break;
                case EnPorton:
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

        if (!moving) {
            switch (this.estado) {
                case Esperando:
                    //aterrizar el avion y cambiar estado a aterrizando
                    this.siguientePosicion = posiciones.esperar.get(posiciones.posicionEsperar);
                    posiciones.posicionEsperar = (posiciones.posicionEsperar + 1) % posiciones.esperar.size();

                    if ((this.x == this.posiciones.esperar.get(2).x && this.y == this.posiciones.esperar.get(2).y) && tienePermisoUsarPista) {
                        //cambiar de estado a aterrizando por la pista activa del momento
                        //pero llamar a los semaforos y eso en la otra maquina de estados
                        if (this.numeroPistaUsada == "01") {
                            this.estado = Estados.Aterrizando01;
                        } else if (this.numeroPistaUsada == "06") {
                            this.estado = Estados.Aterrizando06;
                        } else if (this.numeroPistaUsada == "19") {
                            this.estado = Estados.Aterrizando19;
                        } else {
                            this.estado = Estados.Aterrizando24;
                        }
                        posicion = -1;
                        break;
                    }

                    break;
                case Aterrizando01:
                    posiciones.posicionEsperar = 0;

                    if (this.x == posiciones.aterrizar01.get(posiciones.aterrizar01.size() - 1).x && this.y == posiciones.aterrizar01.get(posiciones.aterrizar01.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a taxeando de 01 a porton
                        this.taxear01Porton();
                        this.estado = Estados.Taxeando01Porton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.aterrizar01.get(posicion);

                    break;
                case Aterrizando06:
                    posiciones.posicionEsperar = 0;

                    if (this.x == posiciones.aterrizar06.get(posiciones.aterrizar06.size() - 1).x && this.y == posiciones.aterrizar06.get(posiciones.aterrizar06.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a taxeando de 06 a porton
                        this.taxear06Porton();
                        this.estado = Estados.Taxeando06Porton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.aterrizar06.get(posicion);
                    break;
                case Aterrizando19:
                    posiciones.posicionEsperar = 0;
                    if (this.x == posiciones.aterrizar19.get(posiciones.aterrizar19.size() - 1).x && this.y == posiciones.aterrizar19.get(posiciones.aterrizar19.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a taxeando de 19 a porton
                        this.taxear19Porton();
                        this.estado = Estados.Taxeando19Porton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.aterrizar19.get(posicion);
                    break;
                case Aterrizando24:
                    posiciones.posicionEsperar = 0;

                    if (this.x == posiciones.aterrizar24.get(posiciones.aterrizar24.size() - 1).x && this.y == posiciones.aterrizar24.get(posiciones.aterrizar24.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a taxeando de 24 a porton
                        this.taxear24Porton();
                        this.estado = Estados.Taxeando24Porton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.aterrizar24.get(posicion);
                    break;
                case Despegando01:

                    if (this.x == posiciones.despegar01.get(posiciones.despegar01.size() - 1).x && this.y == posiciones.despegar01.get(posiciones.despegar01.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a esperando
                        this.estado = Estados.Esperando;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.despegar01.get(posicion);
                    break;
                case Despegando06:

                    if (this.x == posiciones.despegar06.get(posiciones.despegar06.size() - 1).x && this.y == posiciones.despegar06.get(posiciones.despegar06.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a esperando
                        this.estado = Estados.Esperando;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.despegar06.get(posicion);
                    break;
                case Despegando19:

                    if (this.x == posiciones.despegar19.get(posiciones.despegar19.size() - 1).x && this.y == posiciones.despegar19.get(posiciones.despegar19.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a esperando
                        this.estado = Estados.Esperando;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.despegar19.get(posicion);
                    break;
                case Despegando24:

                    if (this.x == posiciones.despegar24.get(posiciones.despegar24.size() - 1).x && this.y == posiciones.despegar24.get(posiciones.despegar24.size() - 1).y) {
                        //ya salio de la pista, lo cambio de estado a esperando
                        this.estado = Estados.Esperando;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.despegar24.get(posicion);
                    break;
                case Taxeando01Porton:

                    if (this.x == posiciones.taxear01porton.get(posiciones.taxear01porton.size() - 1).x && this.y == posiciones.taxear01porton.get(posiciones.taxear01porton.size() - 1).y) {
                        //ya llego al porton, lo cambio de estado a en porton
                        this.estado = Estados.EnPorton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxear01porton.get(posicion);
                    break;
                case Taxeando06Porton:

                    if (this.x == posiciones.taxear06porton.get(posiciones.taxear06porton.size() - 1).x && this.y == posiciones.taxear06porton.get(posiciones.taxear06porton.size() - 1).y) {
                        //ya llego al porton, lo cambio de estado a en porton
                        this.estado = Estados.EnPorton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxear06porton.get(posicion);
                    break;
                case Taxeando19Porton:

                    if (this.x == posiciones.taxear19porton.get(posiciones.taxear19porton.size() - 1).x && this.y == posiciones.taxear19porton.get(posiciones.taxear19porton.size() - 1).y) {
                        //ya llego al porton, lo cambio de estado a en porton
                        this.estado = Estados.EnPorton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxear19porton.get(posicion);
                    break;
                case Taxeando24Porton:

                    if (this.x == posiciones.taxear24porton.get(posiciones.taxear24porton.size() - 1).x && this.y == posiciones.taxear24porton.get(posiciones.taxear24porton.size() - 1).y) {
                        //ya llego al porton, lo cambio de estado a en porton
                        this.estado = Estados.EnPorton;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxear24porton.get(posicion);
                    break;
                case TaxeandoPorton01:

                    if (this.x == posiciones.taxearporton01.get(posiciones.taxearporton01.size() - 1).x && this.y == posiciones.taxearporton01.get(posiciones.taxearporton01.size() - 1).y) {
                        //ya llego a la pista, lo cambio a despegando por la pista 01
                        this.estado = Estados.Despegando01;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxearporton01.get(posicion);
                    break;
                case TaxeandoPorton06:

                    if (this.x == posiciones.taxearporton06.get(posiciones.taxearporton06.size() - 1).x && this.y == posiciones.taxearporton06.get(posiciones.taxearporton06.size() - 1).y) {
                        //ya llego a la pista, lo cambio a despegando por la pista 06
                        this.estado = Estados.Despegando06;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxearporton06.get(posicion);
                    break;
                case TaxeandoPorton19:

                    if (this.x == posiciones.taxearporton19.get(posiciones.taxearporton19.size() - 1).x && this.y == posiciones.taxearporton19.get(posiciones.taxearporton19.size() - 1).y) {
                        //ya llego a la pista, lo cambio a despegando por la pista 19
                        this.estado = Estados.Despegando19;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxearporton19.get(posicion);
                    break;
                case TaxeandoPorton24:

                    if (this.x == posiciones.taxearporton24.get(posiciones.taxearporton24.size() - 1).x && this.y == posiciones.taxearporton24.get(posiciones.taxearporton24.size() - 1).y) {
                        //ya llego a la pista, lo cambio a despegando por la pista 24
                        this.estado = Estados.Despegando24;
                        posicion = -1;
                        break;
                    }
                    this.siguientePosicion = posiciones.taxearporton24.get(posicion);
                    break;
                case EnPorton:
                    //esperar algunos segundos random, y despues ver cual es la pista activa y taxear a ella
                    this.taxearPistaActiva();
                    break;
            }
            moveTo(this.siguientePosicion.x, this.siguientePosicion.y);
            if (!this.siguientePosicion.necesitaPermiso) {
                posicion++;
            }
        }
    }

    public void taxearPistaActiva() {

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
        Despegando01, Despegando06, Despegando19, Despegando24, Esperando, Aterrizando01, Aterrizando06, Aterrizando19, Aterrizando24, Taxeando01Porton, Taxeando06Porton, Taxeando19Porton, Taxeando24Porton, TaxeandoPorton01, TaxeandoPorton06, TaxeandoPorton19, TaxeandoPorton24,

        EnPorton

    }
}


