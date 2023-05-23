public interface IRecurso {
    public Boolean siendoUsado();
    public Boolean getEstaRoto();
    public void setEstaRoto(boolean bool);

    public void cambiarEstadoUsando();
    public void cambiarEstadoDisponible();
}
