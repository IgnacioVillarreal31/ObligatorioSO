public class Posicion {
    protected int x;
    protected int y;
    //indica si debe pedir permiso para ir a la posicion
    public boolean necesitaPermiso;

    public Posicion(int x, int y, boolean necesitaPermiso) {
        this.x = x;
        this.y = y;
        this.necesitaPermiso = necesitaPermiso;
    }

}
