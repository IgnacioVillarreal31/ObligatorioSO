class Avion implements Runnable {
    private String nombre;
    private PistaAterrizaje pista;

    public Avion(String nombre, PistaAterrizaje pista) {
        this.nombre = nombre;
        this.pista = pista;
    }

    public void run() {
        pista.aterrizar(nombre);

        // Simulación de tiempo de aterrizaje
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        pista.despegar(nombre);
    }
}
