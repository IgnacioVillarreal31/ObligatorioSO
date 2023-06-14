/*
Recorre constantemente la PriorityBlockingQueue con los aviones que pidieron permiso para aterizar,
y les da permiso.
 */


import javax.swing.*;

public class Recorrer extends Thread implements Runnable {
    public Aeropuerto aeropuerto;

    private final int DELAY = 100;

    public Recorrer(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    @Override
    public void run() {

        while (true) {
            //recorrer las tres listas de prioridad, una para aterrizar, otra para usar la pista1 y otra para usar la pista2
            while (!aeropuerto.aterrizar.isEmpty()) {
                Avion avion = aeropuerto.aterrizar.poll();
                avion.estado = Avion.Estados.Aterrizar;
                avion.continuar = true;
                synchronized (this) {
                    try {
                        this.wait();
                        System.out.println("LEVANTO");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                /*
                try {
                    //verificar cual es la pistaActiva del momento
                    //avion.aterrizar();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/
            }
        }
    }

}
