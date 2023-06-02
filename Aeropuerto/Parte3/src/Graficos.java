import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Graficos extends JFrame implements Runnable {

    private static final int WIDTH = 1300;
    private static final int HEIGHT = 700;
    public final Aeropuerto aeropuerto;

    public Graficos(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        setTitle("Aeropuerto");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        try {
            setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/aero.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + "," + y);
            }
        });
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Avion avion : aeropuerto.aviones) {
            avion.draw(g);
        }
    }

    @Override
    public void run() {

        Timer timer = new Timer(40, e -> {
            for (Avion avion : aeropuerto.aviones) {
                avion.nextPosition();
                avion.move();
            }
            repaint();
        });
        timer.start();
    }

}
