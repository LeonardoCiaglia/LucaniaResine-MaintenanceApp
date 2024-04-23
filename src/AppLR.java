import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

class AppLR {
    public static void main(String[] args) throws Exception {
        // Frame di Login
        LFrame LoginFrame = new LFrame("Login",new ImageIcon("img/logoAzienda.jpg"), 600, 600,true);
        
        // Contenitore Intermedio
        Container contentPane = LoginFrame.getContentPane();

        // Pannello
        JPanel LoginPanel = new JPanel();

        // Layout con GridBagConstraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 5, 5);
        LoginPanel.setLayout(new GridBagLayout());

        contentPane.add(LoginPanel);

        // Titolo
        LLabel lblTitle = new LLabel("Lucania Resine S.R.L.", null);
        lblTitle.setFontDimension(40f);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;

        LoginPanel.add(lblTitle,c);

        // Immagine dell' Account
        LLabel ImgAccount = new LLabel(null, new ImageIcon("img/Account.png"));

        c.gridy++;

        LoginPanel.add(ImgAccount,c);

        // TextField dove viene inserito l'username
        LTextField tfUser = new LTextField("User:",20);
        tfUser.setFontDimension(20f);

        c.gridy++;
        
        LoginPanel.add(tfUser,c);

        // TextField dove viene inserita la password
        JPasswordField JfPassword= new JPasswordField("",20);
        JfPassword.setFont(JfPassword.getFont().deriveFont(19f));
        c.gridy++;

        LoginPanel.add(JfPassword,c);

        LCheckBox showPassword = new LCheckBox(new ImageIcon ("img/Show.png"),JfPassword);
        c.gridx = 1;

        LoginPanel.add(showPassword, c);

        // Bottone per verificare le credenziali
        LButton btnLogin = new LButton("Login", new Color(33, 150, 243), Color.LIGHT_GRAY, tfUser, JfPassword,null);
        btnLogin.setFontDimension(15f);

        c.gridy++;
        c.gridx = 0;

        LoginPanel.add(btnLogin,c);

        // Ricalcola le posizioni e le dimensioni dei componenti del LoginFrame
        LoginFrame.revalidate();
    }
}
