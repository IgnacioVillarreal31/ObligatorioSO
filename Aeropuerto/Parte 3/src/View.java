import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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

        ImageIcon img = new ImageIcon("src/avion.png");
        frame.setIconImage(img.getImage());

        frame.add(avionPane);
        frame.setTitle("Sincronización Aeropuerto");

        String[] columnas = {"ID", "ESTADO", "PRIORIDAD"};
        String[][] filas = new String[model.getBalls().size()][3];
        int i = 0;
        DefaultTableModel m = (DefaultTableModel) avionPane.tabla.getModel();
        m.addColumn(columnas[0].toString());
        m.addColumn(columnas[1].toString());
        m.addColumn(columnas[2].toString());
        avionPane.filasTabla = new HashMap<String, Integer>();
        for (Avion b : model.getBalls()) {
            //agrega los aviones a la pantalla
            avionPane.filasTabla.put(b.nombre, i);
            i++;
            avionPane.add(b.getPanel());
            m.addRow(new Object[]{b.nombre, b.getEstado().toString(), String.valueOf(b.getPrioridad())});
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
