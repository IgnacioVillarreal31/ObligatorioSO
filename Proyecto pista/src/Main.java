public class Main {
    public static void main(String[] args) {
        PistaAterrizaje pista = new PistaAterrizaje();

        Thread avion1 = new Thread(new Avion("Avion1", pista));
        Thread avion2 = new Thread(new Avion("Avion2", pista));

        avion1.start();
        avion2.start();
    }
}