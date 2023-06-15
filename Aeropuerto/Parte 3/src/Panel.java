import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Panel extends JPanel {
    private JPanel panel;
    private JLabel icono;
    private JLabel estado;
    private JLabel id;
    private RotatedIcon ri;
    private boolean visible;


    public Panel(String ID, String estado, ImageIcon icono) {
        this.visible = true;
        this.icono = new JLabel();
        this.estado = new JLabel();
        this.id = new JLabel();
        this.ri = new RotatedIcon(icono, 0, true);
        Random rand = new Random();
        //Color color = Color.getHSBColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        Color color = Color.RED;
        this.icono.setIcon(this.ri);
        this.id.setText(ID);
        this.id.setForeground(color);
        this.estado.setText(estado);
        this.estado.setForeground(color);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        setSize(new Dimension(100, 60));
        add(this.icono);
        add(this.id);
        add(this.estado);
        this.icono.setVisible(true);
        this.id.setVisible(true);
        this.estado.setVisible(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                /*synchronized (this) {
                    try {
                    //no siver poner un wait aca porque bloquea todo, debe ser porque esta dentro del panel
                        this.wait(5000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }*/
                visible = !visible;
                hacerVisible(visible);
            }

        });
        setVisible(true);

    }

    public void cambiarEstado(String estado) {
        this.estado.setText(estado);
    }

    public void rotarIcono(double grados) {
        this.ri.setDegrees(grados);
    }

    public void hacerVisible(boolean visible) {
        this.id.setVisible(visible);
        this.estado.setVisible(visible);
    }

}
