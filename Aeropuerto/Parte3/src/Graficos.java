import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Graficos extends JFrame implements Runnable {

    public JTable tabla;
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 700;
    public static final int DELAY = 40;
    public final Aeropuerto aeropuerto;

    //posiciones de los aviones en la tabla
    private HashMap<String, Integer> filasTabla;

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
        String[] columnas = {"ID", "ESTADO", "PRIORIDAD"};
        String[][] filas = new String[aeropuerto.aviones.size()][3];
        filasTabla = new HashMap<String, Integer>();
        //agrega los aviones a la pantalla
        int i = 0;
        for (Avion avion : aeropuerto.aviones.values()) {
            filas[i][0] = avion.nombre;
            filas[i][1] = avion.estado.toString();
            filas[i][2] = String.valueOf(avion.prioridad);
            filasTabla.put(avion.nombre, i);
            add(avion.panel);
            i++;
        }
        tabla = new JTable(filas, columnas) {
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
                    if (col == 2 && !tabla.getValueAt(row, 2).equals("0")) {
                        //pedir prioridad para aterrizar
                        aeropuerto.aviones.get(tabla.getValueAt(row, 0)).pedirPrioridadAterrizar();
                    }
                }
            }
        });

        //        tabla.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    //apreto enter sobre la tabla, ver cual celda esta seleccionada
//                    // y cambiar la prioridad del avion si esta volando. Si esta en el piso,
//                    // la prioridad es 1 para que no haya aplazamiento indefinido con los otros aviones
//                    System.out.println(tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()));
//
//                }
//            }
//
//        });
        tabla.setSize(300, 300);
        JScrollPane jsp = new JScrollPane(tabla);
        jsp.setSize(300, 300);
        jsp.setLocation(1000, 400);
        add(jsp);
        this.setVisible(true);
        new Thread(this).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);


        for (Avion avion : aeropuerto.aviones.values()) {
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
        System.out.println("Thread actual graficos: " + Thread.currentThread().getName());
        Timer timer = new Timer(DELAY, e -> {
            for (Avion avion : aeropuerto.aviones.values()) {
                tabla.setValueAt(avion.estado.toString(), this.filasTabla.get(avion.nombre), 1);
                tabla.setValueAt(String.valueOf(avion.prioridad), this.filasTabla.get(avion.nombre), 2);
                try {
                    avion.nextPosition();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                avion.move();
            }
            repaint();
        });
        timer.start();
    }

}
