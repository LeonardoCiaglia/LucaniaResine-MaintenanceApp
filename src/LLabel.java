import javax.swing.ImageIcon;
import javax.swing.JLabel;

class LLabel extends JLabel {
    // Costruttore
    public LLabel(String text, ImageIcon img){
        super(text);
        setIcon(img);
    }

    // Funzione per modificare la dimensione del font
    public void setFontDimension(float dimension){
        setFont(getFont().deriveFont(dimension));
    }
}
