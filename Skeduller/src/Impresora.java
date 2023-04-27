public class Impresora implements IRecurso {
    public Boolean siendoUtilizado = false;
    @Override
    public void cambiarEstadoUsoando() {
        siendoUtilizado = true;
    }
    @Override
    public void cambiarEstadoDisponible() {
        siendoUtilizado = false;
    }
}
