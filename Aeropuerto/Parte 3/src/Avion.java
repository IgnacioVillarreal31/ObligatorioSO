import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Avion implements Comparable<Avion> {

    //Ball attributes
    private static final int SIZE = 10;  //diameter
    private int x, y;  // Position
    private final Color color;
    private Observer observer;  //to be notified on changes

    public Panel panel;

    public Posiciones posiciones;

    public String nombre;

    public Estados estado;

    private Posicion siguientePosicion;

    private int targetX;
    private int targetY;
    private boolean moving = false;

    private boolean tienePermisoAterrizar = false;

    protected int prioridad;

    protected long timestamp;

    private Aeropuerto aeropuerto;

    public String numeroPistaUsada;

    private Pista pistaUsada;


    public Avion(String nombre, Estados estado, Aeropuerto aeropuerto) {
        this.nombre = nombre;
        this.estado = estado;
        this.targetX = 50;
        this.targetY = 50;
        this.timestamp = System.nanoTime();
        this.prioridad = 1;
        this.siguientePosicion = new Posicion(x, y, false);
        this.aeropuerto = aeropuerto;
        ImageIcon avion = null;
        try {
            avion = new ImageIcon(ImageIO.read(new File("src/avion.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.posiciones = new Posiciones();
        this.panel = new Panel(this.nombre, this.estado.toString(), avion);
        Random rnd = new Random();
        color = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public synchronized void setPistaUsada(Pista pista) {
        this.pistaUsada = pista;
    }

    public synchronized Pista getPistaUsada() {
        return this.pistaUsada;
    }

    public synchronized Aeropuerto getAeropuerto() {
        return this.aeropuerto;
    }

    public synchronized boolean getTienePermisoUsarPista() {
        return this.tienePermisoAterrizar;
    }

    public synchronized void setTienePermisoUsarPista(boolean permiso) {
        this.tienePermisoAterrizar = permiso;
    }

    public synchronized int getTargetX() {
        return this.targetX;
    }

    public synchronized void setTargetX(int x) {
        this.targetX = x;
    }

    public synchronized int getTargetY() {
        return this.targetY;
    }

    public synchronized void setTargetY(int y) {
        this.targetY = y;
    }

    public synchronized void setSiguientePosicion(Posicion pos) {
        this.siguientePosicion = pos;
    }

    public synchronized Posicion getSiguientePosicion() {
        return this.siguientePosicion;
    }

    public synchronized boolean getMoving() {
        return this.moving;
    }

    public synchronized void setMoving(boolean moving) {
        this.moving = moving;
    }

    Color getColor() {
        return color;
    }

    int getSize() {
        return SIZE;
    }

    synchronized int getX() {
        return x;
    }

    synchronized Panel getPanel() {
        return this.panel;
    }

    synchronized void setX(int x) {
        this.x = x;
        notifyObserver();
    }

    synchronized int getY() {
        return y;
    }

    synchronized void setY(int y) {
        this.y = y;
        notifyObserver();
    }

    void registerObserver(Observer observer) {
        this.observer = observer;
    }

    void notifyObserver() {
        if (observer == null) return;
        observer.onObservableChanged();
    }

    public synchronized Estados getEstado() {
        return this.estado;
    }

    public synchronized void setEstado(Estados estado) {
        this.estado = estado;
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

    public synchronized void setNumeroPistaUsada(String pista) {
        this.numeroPistaUsada = pista;
    }

    public synchronized String getNumeroPistaUsada() {
        return this.numeroPistaUsada;
    }

    public enum Estados {
        Despegando01, Despegando06, Despegando19, Despegando24,
        Esperando,
        Aterrizando01, Aterrizando06, Aterrizando19, Aterrizando24,
        Taxeando01Porton, Taxeando06Porton, Taxeando19Porton, Taxeando24Porton,
        TaxeandoPorton01, TaxeandoPorton06, TaxeandoPorton19, TaxeandoPorton24,
        Estacionando01, Estacionando06, Estacionando19, Estacionando24,
        Estacionado,
        Aterrizar,
        EnPorton
    }
}
