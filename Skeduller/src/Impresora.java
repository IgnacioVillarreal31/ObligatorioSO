public class Impresora implements IRecurso {
    public Boolean siendoUtilizado = false;
    @Override
    public void cambiarEstadoUso() {
        if (siendoUtilizado == false){
            siendoUtilizado = true;
        }else {
            siendoUtilizado = false;
        }
    }
}
