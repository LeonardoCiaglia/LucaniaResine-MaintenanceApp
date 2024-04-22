import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextField;

class LTextField extends JTextField implements MouseListener{
    // Variabili
    private String text;

    // Costrutore
    public LTextField(String text,int colonne) {
        super(text,colonne);
        this.text = text;
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        // Controlla che il testo della TF sia uguale a quello attuale
        if(getText().equals(text)){
            setText("");
            setEditable(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Controlla che il testo della TF sia uguale a quello attuale
        if(getText().equals("")){
            setText(text);
            setEditable(false);
        }
    }

    public void setFontDimension(float dimension){
        setFont(getFont().deriveFont(dimension));
    }

}