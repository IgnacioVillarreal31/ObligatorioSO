import java.util.concurrent.Semaphore;


public class Proyecto {
    public int i = 0;

    public static void main(String[] args) {
        Semaphore rec = new Semaphore(1); //semaforo de recorrer para que solo permita que uno aterrice por vez
        Model model = new Model();
        View view = new View(model);
        Aeropuerto aeropuerto = new Aeropuerto();
        aeropuerto.setPistaActiva("19");
        for (int i = 0; i < 5; i++) {
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