import java.util.ArrayList;
import java.util.List;

public class Model {

    private final ArrayList<Avion> avions;
    private final int width, height;

    public Aeropuerto aeropuerto;

    Model(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        avions = new ArrayList<>();
        width = 1300;
        height = 700;
    }

    boolean addBall(Avion avion) {
        return avions.add(avion);
    }

    List<Avion> getBalls() {
        return new ArrayList<>(avions); //return a copy of balls
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }
}
