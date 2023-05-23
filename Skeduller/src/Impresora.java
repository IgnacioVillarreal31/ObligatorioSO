public class Impresora implements IRecurso {
    public Boolean siendoUtilizado;
    public Boolean estaRoto;

    public Impresora() {
        this.siendoUtilizado = false;
        this.estaRoto = false;
    }

    @Override
    public Boolean siendoUsado(){
        return siendoUtilizado;
    }

    @Override
    public Boolean getEstaRoto() {
        return estaRoto;
    }

    @Override
    public void setEstaRoto(boolean bool) {
        this.estaRoto = bool;
    }

    @Override
    public void cambiarEstadoUsando() {
        siendoUtilizado = true;
    }
    @Override
    public void cambiarEstadoDisponible() {
        siendoUtilizado = false;
    }
}
