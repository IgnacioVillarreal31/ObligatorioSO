import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

public class Main {
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 700;
    private static final int SQUARE_SIZE = 25;
    private static final int NUM_SQUARES = 1;

    public static void main(String[] args) {
        ImageIcon avion = null;
        try {
            avion = new ImageIcon(ImageIO.read(new File("src/avion.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Aeropuerto aero = new Aeropuerto();
        Avion a1 = new Avion(aero, 1, "A1", 10, Avion.Estados.Esperando, 15, 15, avion);
        Avion a2 = new Avion(aero, 1, "A2", 10, Avion.Estados.Esperando, 15, 200, avion);
        Avion a3 = new Avion(aero, 1, "A3", 10, Avion.Estados.Esperando, 15, 500, avion);
        Avion a4 = new Avion(aero, 1, "A4", 10, Avion.Estados.Esperando, 200, 500, avion);
        Avion a5 = new Avion(aero, 1, "A5", 10, Avion.Estados.Esperando, 500, 500, avion);
        aero.agregar(a1);
        aero.agregar(a2);
        aero.agregar(a3);
        aero.agregar(a4);
        aero.agregar(a5);
        /*
        aero.cola.offer(a1);
        aero.cola.offer(a2);
        aero.cola.offer(a3);
        aero.cola.offer(a4);
        aero.cola.offer(a5);

        //System.out.println(aero.cola);
        //    aero.cola.remove(a3);
        a3.pedirPrioridad();
        //  aero.cola.offer(a3);
        // a3.pedirPrioridad();
        //aero.cola.remove(a5);
        a5.pedirPrioridad();
        //aero.cola.offer(a5);
        //a5.pedirPrioridad();
/*
        while (!aero.cola.isEmpty()) {
            Avion avion = aero.cola.poll();
            System.out.println(avion.nombre + " ");
        }
*/


        aero.run();

    }
}