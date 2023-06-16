/*
Recorre constantemente la PriorityBlockingQueue con los aviones que pidieron permiso para aterizar,
y les da permiso.
 */


import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Recorrer extends Thread implements Runnable {
    public Aeropuerto aeropuerto;
    public ReentrantLock lock;
    public Semaphore semaforo;

    private final int DELAY = 100;

    public Recorrer(Aeropuerto aeropuerto, Semaphore s) {
        this.aeropuerto = aeropuerto;
        lock = new ReentrantLock();
        semaforo = s;
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Thread actual recorrer: " + Thread.currentThread().getName());

        while (true) {
            //recorrer las tres listas de prioridad, una para aterrizar, otra para usar la pista1 y otra para usar la pista2
            while (!aeropuerto.aterrizar.isEmpty()) {
                try {
                    semaforo.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Avion avion = aeropuerto.aterrizar.poll();
                avion.estado = Avion.Estados.Aterrizar;
                avion.continuar = true;



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
