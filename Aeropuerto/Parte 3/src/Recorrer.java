/*
Recorre constantemente la PriorityBlockingQueue con los aviones que pidieron permiso para aterizar,
y les da permiso.
 */


import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Recorrer extends Thread implements Runnable {
    private Semaphore semaforo;

    private Aeropuerto aeropuerto;
    private final int DELAY = 100;

    public Recorrer(Aeropuerto aeropuerto, Semaphore s) {
        this.aeropuerto = aeropuerto;
        semaforo = s;
        new Thread(this).start();
    }

    @Override
    public void run() {

        while (true) {
            //recorrer las tres listas de prioridad, una para aterrizar, otra para usar la pista1 y otra para usar la pista2
            while (!aeropuerto.getAvionesAterrizar().isEmpty()) {
                try {
                    semaforo.acquire();
                    Avion avion = aeropuerto.getAvionesAterrizar().poll();
                    avion.setEstado(Avion.Estados.Aterrizar);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

/*
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
