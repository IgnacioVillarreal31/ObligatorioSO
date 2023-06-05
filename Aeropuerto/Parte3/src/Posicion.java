public class Posicion {
    protected int x;
    protected int y;
    //indica si debe pedir permiso para ir a la posicion
    public boolean permiso;

    public Posicion(int x, int y, boolean permiso) {
        this.x = x;
        this.y = y;
        this.permiso = permiso;
    }

}
