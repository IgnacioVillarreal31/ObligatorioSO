public class Impresora implements IRecurso {
    public Boolean siendoUtilizado;

    public Impresora() {
        this.siendoUtilizado = false;
    }

    @Override
    public Boolean siendoUsado(){
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
