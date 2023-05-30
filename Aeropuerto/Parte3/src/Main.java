import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {

        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();

        Aeropuerto aero = new Aeropuerto();
        Avion a1 = new Avion(aero, 1, "A1", 10, Avion.Estados.Volando);
        Avion a2 = new Avion(aero, 1, "A2", 10, Avion.Estados.Volando);
        Avion a3 = new Avion(aero, 1, "A3", 10, Avion.Estados.Volando);
        Avion a4 = new Avion(aero, 1, "A4", 10, Avion.Estados.Volando);
        Avion a5 = new Avion(aero, 1, "A5", 10, Avion.Estados.Volando);
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