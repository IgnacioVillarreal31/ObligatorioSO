import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class View {
    private final AvionPane avionPane;

    private Model model;

    public View(Model model) {
        ImageIcon avion = null;
        this.model = model;
        try {
            avion = new ImageIcon(ImageIO.read(new File("src/avion.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        avionPane = new AvionPane(model);
    }

    void createAndShowGui() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
/*
        try {
            frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/aero.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        frame.add(avionPane);
        for (Avion b : model.getBalls()) {
            avionPane.add(b.getPanel());
        }
        //frame.pack();
        frame.setSize(model.getWidth() + 15, model.getHeight() + 15);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    Observer getObserver() {
        return avionPane;
    }
}
