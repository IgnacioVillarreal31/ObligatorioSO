import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AvionPane extends JPanel implements Observer {

    private final Model model;
    private BufferedImage image;

    public AvionPane(Model model) {
        this.model = model;
        setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        try {
            image = ImageIO.read(new File("src/aero.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        /*
        for (Ball b : model.getBalls()) {
            this.add(b.getPanel());
        }*/
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Avion b : model.getBalls()) {
            g.setColor(b.getColor());
            g.fillOval(b.getX(), b.getY(), b.getSize(), b.getSize());
            //b.panel.setLocation(b.getX(), b.getY());
            b.getPanel().setLocation(b.getX(), b.getY());
        }
        g.drawImage(image, 20, 50, null);
        for (Avion b : model.getBalls()) {
            HashMap pos = b.posiciones.mapa;
            pos.forEach((key, value) -> {
                for (Posicion p : (ArrayList<Posicion>) value) {
                    Color color = Color.RED;
                    g.setColor(color);
                    g.fillOval(p.x, p.y, 10, 10);
                }
            });
            break;
        }
    }

    @Override
    public void onObservableChanged() {
        repaint(); //when a change was notified
    }
}

