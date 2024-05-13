import javax.swing.ImageIcon;
import javax.swing.JFrame;

class LFrame extends JFrame{
    // Costruttore
    public LFrame(String title,ImageIcon icon,int width,int height,boolean isVisible){
        super(title);
        setIconImage(icon.getImage());
        setSize(width,height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(isVisible);
    }
}
