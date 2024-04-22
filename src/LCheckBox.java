import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

class LCheckBox extends JCheckBox implements ActionListener{
    // Variabili
    private JPasswordField JfPassword;

    // Costruttore
    public LCheckBox(ImageIcon Icon, JPasswordField jfPass){
        super(Icon);
        this.JfPassword = jfPass;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // Controllo se il CheckBox viene selezionato
        if(isSelected()) JfPassword.setEchoChar((char) 0); // La password viene mostrata
        else JfPassword.setEchoChar('\u2022'); // la password viene mascherata
    }
}