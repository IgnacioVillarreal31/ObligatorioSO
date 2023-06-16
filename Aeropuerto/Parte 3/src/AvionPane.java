import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AvionPane extends JPanel implements Observer {

    private final Model model;
    private BufferedImage image;

    public JTable tabla;

    public HashMap<String, Integer> filasTabla;


    public AvionPane(Model model) {
        this.model = model;
        setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        setLayout(null);

        try {
            image = ImageIO.read(new File("src/aero.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //String[] columnas = {"ID", "ESTADO", "PRIORIDAD"};
        /*String[][] filas = new String[model.getBalls().size()][3];
        filasTabla = new HashMap<String, Integer>();
        //agrega los aviones a la pantalla
        int i = 0;
        for (Avion avion : model.getBalls()) {
            filas[i][0] = avion.nombre;
            filas[i][1] = avion.getEstado().toString();
            filas[i][2] = String.valueOf(avion.getPrioridad());
            filasTabla.put(avion.nombre, i);
            i++;
        }*/
        tabla = new JTable() {
            @Override
            public boolean isCellEditable(int row, int col) {
                // return this.getValueAt(row, 1) == "Esperando" && col == 2;
                return false;
            }
        };


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + "," + y);
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabla.rowAtPoint(evt.getPoint());
                int col = tabla.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    if (col == 2 && tabla.getValueAt(row, 1).equals("Esperando") && !tabla.getValueAt(row, 2).equals("0")) {
                        //pedir prioridad para aterrizar
                        //aeropuerto.aviones.get(tabla.getValueAt(row, 0)).pedirPrioridadAterrizar();
                        for (Avion avion : model.getBalls()) {
                            if (avion.nombre == tabla.getValueAt(row, 0).toString()) {
                                avion.pedirPrioridadAterrizar();
                                break;
                            }
                        }
                    }
                }
            }
        });
        JButton b1 = new JButton();
        b1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                model.aeropuerto.cambiarDireccionVientoRandom();
                model.aeropuerto.elegirPistaActiva();
                b1.setText("Pista Activa: " + model.aeropuerto.getPistaActiva()[1].toString());
            }
        });
        b1.setSize(300, 40);
        b1.setVisible(true);
        b1.setText("Pista Activa: " + model.aeropuerto.getPistaActiva()[1].toString());
        b1.setLocation(1000, 360);
        tabla.setSize(300, 300);
        JScrollPane jsp = new JScrollPane(tabla);
        jsp.setSize(300, 300);
        jsp.setLocation(1000, 400);
        add(b1);
        add(jsp);

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Avion b : model.getBalls()) {
            try {
                tabla.setValueAt(b.getEstado().toString(), this.filasTabla.get(b.nombre), 1);
                tabla.setValueAt(String.valueOf(b.getPrioridad()), this.filasTabla.get(b.nombre), 2);
            } catch (Exception e) {
                System.out.println("err");
            }
            g.setColor(b.getColor());
            g.fillOval(b.getX(), b.getY(), b.getSize(), b.getSize());
            //b.panel.setLocation(b.getX(), b.getY());
            b.getPanel().setLocation(b.getX(), b.getY());
        }
        g.drawImage(image, 20, 30, null);
        //dibuja los caminos por pantalla
    /*
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
    */
    }

    @Override
    public void onObservableChanged() {
        repaint(); //when a change was notified
    }
}

