public class Impresora implements IRecurso {
    //Creación de variables
    public Boolean siendoUtilizado;
    public Boolean estaRoto;

    //Inicialización de variables
    public Impresora() {
        this.siendoUtilizado = false;
        this.estaRoto = false;
    }

    //Métodos para ver el estado de la impresora (getters)
    @Override
    public Boolean siendoUsado(){
        return siendoUtilizado;
    }

    @Override
    public Boolean getEstaRoto() {
        return estaRoto;
    }

    //Métodos para establecer el estado de la impresora (setters)
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
