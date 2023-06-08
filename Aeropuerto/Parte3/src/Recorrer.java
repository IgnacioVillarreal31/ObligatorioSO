/*
Recorre constantemente la PriorityBlockingQueue con los aviones que pidieron permiso para aterizar,
y les da permiso.
 */


public class Recorrer implements Runnable {
    public Aeropuerto aeropuerto;

    public Recorrer(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    @Override
    public void run() {
        while (true) {
            while (!aeropuerto.aterrizar.isEmpty()) {
                Avion avion = aeropuerto.aterrizar.poll();
                try {
                    //verificar cual es la pistaActiva del momento
                    avion.aterrizar();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
