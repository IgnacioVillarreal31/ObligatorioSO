import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Graficos extends JFrame implements Runnable {

    private static final int WIDTH = 1300;
    private static final int HEIGHT = 700;
    private static final int DELAY = 40;
    public final Aeropuerto aeropuerto;

    public Graficos(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        setTitle("Aeropuerto");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);


        try {
            setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/aero.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //agrega los aviones a la pantalla
        for (Avion avion : aeropuerto.aviones) {
            add(avion.panel);
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
            //avion.draw(g);
            HashMap pos = avion.posiciones.mapa;
            Random rand = new Random();
            int[] arr = {0};
            int a = 50;
            int b = 50;
            int c = 50;
            pos.forEach((key, value) -> {
                arr[0] = (arr[0] + 15) % 255;
                for (Posicion p : (ArrayList<Posicion>) value) {
                    Color color = Color.RED;
                    g.setColor(color);
                    g.fillOval(p.x, p.y, 10, 10);
                }
            });
            return;
        }

    }

    @Override
    public void run() {

        Timer timer = new Timer(DELAY, e -> {
            for (Avion avion : aeropuerto.aviones) {
                avion.nextPosition();
                avion.move();
            }
            repaint();
        });
        timer.start();
    }

}
