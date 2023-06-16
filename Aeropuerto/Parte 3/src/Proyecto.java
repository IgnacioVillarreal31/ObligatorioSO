import java.util.concurrent.Semaphore;


public class Proyecto {
    public static int numAviones = 5;

    public static void main(String[] args) {
        Semaphore rec = new Semaphore(1); //semaforo de recorrer para que solo permita que uno aterrice por vez
        Aeropuerto aeropuerto = new Aeropuerto(rec);
        aeropuerto.setPistaActiva("19");
        Model model = new Model(aeropuerto);
        View view = new View(model);
        for (int i = 0; i < numAviones; i++) {
            Avion b = new Avion("Avion " + i, Avion.Estados.Esperando, aeropuerto); //construct  a ball
            model.addBall(b);    //add it to the model
            b.registerObserver(view.getObserver());  //register view as an observer to it
            new AvionAnimator(b, model.getWidth(), model.getHeight(), i, rec); //start a thread to update it
        }
        new Recorrer(aeropuerto, rec).start();
        view.createAndShowGui();
    }
}

interface Observer {
    void onObservableChanged();
}