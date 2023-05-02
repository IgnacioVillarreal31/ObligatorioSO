public class Impresora implements IRecurso {
    public Boolean siendoUtilizado;

    public Impresora() {
        this.siendoUtilizado = false;
    }

    public Boolean getEstado(){
        return siendoUtilizado;
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
