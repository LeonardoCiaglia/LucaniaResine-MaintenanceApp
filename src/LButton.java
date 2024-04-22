import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Vector;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class LButton extends JButton implements ActionListener {
    // Variabili
    private LTextField tfUser;
    private JPasswordField JfPassword = new JPasswordField();
    private DefaultTableModel model;

    // Costruttore
    public LButton(String Text,Color textColor,Color textBackground,LTextField tfUser, JPasswordField JfPassword, DefaultTableModel model){
        super(Text);
        this.tfUser = tfUser;
        this.JfPassword= JfPassword;
        this.model = model;
        setForeground(textColor);
        setBackground(textBackground);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        char[] CharPass = JfPassword.getPassword();
        String StrPass = new String(CharPass);

        // Azioni dei Bottoni in base al testo
       if(getText().equals("Login")){
            if(!tfUser.getText().equals("User:") && !StrPass.equals("")) checkCredentials();
            else JOptionPane.showMessageDialog(null,"Inserisci User e Password","info",JOptionPane.INFORMATION_MESSAGE);
        }else if(getText().equals("Segnala")) ReportsWriter();
        else if(getText().equals("Elenco Segnalazioni")) OpenReportFile();
        else if(getText().equals("Corrugato")) showCorrugato();
        else if (getText().equals("Rotazionale")) showRotazionale();
        else if(getText().equals("PVC")) showPVC();
        else if(getText().equals("Polietilene")) showPolietilene();
    }

    // Funzione per cambiare la dimensione del Font 
    public void setFontDimension(float dimension){
        setFont(getFont().deriveFont(dimension));
    }

    // Funzione per confrontare le credenziali inserite con quelle nel file
    private void checkCredentials() {
        String AccountPath = "Lucania Resine/src/File/Accounts.csv";

        try {
            File file = new File(AccountPath); // Oggetto File
            // Try con risorse in cui viene creato l'oggetto scanner
            try (Scanner FileReader = new Scanner(file)) {
                // While che scorre finche non ci sono più linee nel file
                while (FileReader.hasNextLine()) {
                    String line = FileReader.nextLine();
                    String[] parts = line.split(";"); // Divide ogni linea in due parti separate da ";"

                    // Controllo se ci sono abbastanza dati per fare il confronto
                    if (parts.length >= 2) {
                        String usernameFromFile = parts[0]; 
                        String passwordFromFile = parts[1];
                        char[] CharPass = JfPassword.getPassword();
                        String StrPass = new String(CharPass);

                        // Controllo se le credenziali corrispondo a quelle nel file
                        if (tfUser.getText().equals(usernameFromFile) && StrPass.equals(passwordFromFile)) {
                            System.out.println("Credenziali valide.");

                            // Controlla il reparto associato all'account
                            if(parts[3].equals("PVC")) showPVC();
                            else if(parts[3].equals("Rotazionale")) showRotazionale();
                            else if(parts[3].equals("Polietilene")) showPolietilene();
                            else if(parts[3].equals("Corrugato")) showCorrugato();
                            else if(parts[3].equals("No")) showAll();

                            return;
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Mi dispiace ma non ha accesso all'app", "Attenzione", JOptionPane.WARNING_MESSAGE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore nella lettura del file", "info", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fuznione per scrivere le segalazioni sul file
    public void ReportsWriter(){
        Vector<Report> V_Reports = new Vector<>();

        // For per vedere se ci sono segnalazioni 
        for(int i = 0; i < model.getRowCount(); i++){
            Object machine = model.getValueAt(i, 0);
            Object value = model.getValueAt(i, 1);
            if(value != null && !value.toString().isEmpty()){
                V_Reports.add(new Report(machine.toString() ,value.toString()));
            }
        }

        // Controllo se ci sono elementi nel Vector
        if(V_Reports.size() > 0){
            // Codice per scrivere le segnalazioni sul file
            try (PrintWriter writer = new PrintWriter(new FileWriter("Lucania Resine/src/File/Segnalazioni.csv", true))) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // Formato data e ora

                // for each per scrivere ogni segnalazione nel file con la data e l'ora attuali
                for (Report report : V_Reports) {
                    String DateTime = dtf.format(LocalDateTime.now()); // Ottieni la data e l'ora attuali
                    writer.println(tfUser.getText() +";"+ report.getMachine() +";"+ report.getReport() + ";" + DateTime +";No"); // Scrivi la segnalazione seguita dalla data e ora nel file
                }

                for(int i = 0; i < model.getRowCount(); i++){
                    model.setValueAt("",i,1);
                }

                JOptionPane.showMessageDialog(null, "Segnalazione avvenuta con successo!", "info", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException j) {
                JOptionPane.showMessageDialog(null, "Errore durante la scrittura sul file!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                System.err.println("Errore durante la scrittura delle segnalazioni su file: " + j.getMessage());
            }
        }
    }

    // Funzione per aprire il file delle Segnalazioni
    private void OpenReportFile(){
        String filePath = "Lucania Resine/src/File/Segnalazioni.csv";

        try {
            File file = new File(filePath); // Oggetto File

            // Controlla se il Desktop è supportato
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop(); // Oggetto Desktop
                
                // Controlla se il file esiste
                if (file.exists()) desktop.open(file); // Apri il file con il programma predefinito associato al suo tipo di file
                else JOptionPane.showMessageDialog(null, "Il file non esiste!", "Attenzione", JOptionPane.WARNING_MESSAGE);

            } else JOptionPane.showMessageDialog(null, "Desktop non supportato.", "Attenzione", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCorrugato(){
        showReparto("Corrugato", "C", 4);
    }

    private void showPVC(){
        showReparto("PVC", "P", 2);
    }

    private void showRotazionale(){
        showReparto("Rotazionale", "R", 3);
    }

    private void showPolietilene(){
        showReparto("Polietilene", "L", 5);
    }

    
    // Funzione per far scegliere al manutentore il reparto 
    private void showAll(){
        showReparto("Manutentore", "L", 5);
    }

    // Mostra il reparto in base al parametro passato
    private void showReparto(String Ward,String MachineName,int nMachine){

        // Frame
        LFrame Frame = new LFrame(Ward,new ImageIcon("Lucania Resine/src/img/logoAzienda.jpg"), 600, 600,true);
        
        // Contenitore Intermedio
        Container contentPane = Frame.getContentPane();

        // Pannello
        JPanel Panel = new JPanel();

        // Layout con GridBagConstraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 5, 5);
        
        Panel.setLayout(new GridBagLayout());

        contentPane.add(Panel);

        // Titolo 
        LLabel lblTitle = new LLabel("Lucania Resine - "+Ward, null);
        lblTitle.setFontDimension(40f);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;

        Panel.add(lblTitle,c);

        if(Ward.equals("Manutentore")){
            // Bottoni per visualizzare i vari reparti
            String[] ButtonWard = {"Corrugato", "PVC", "Rotazionale", "Polietilene", "Elenco Segnalazioni"};

            for(String Department: ButtonWard){
                LButton btnWard = new LButton(Department, new Color(33, 150, 243), Color.lightGray, null, new JPasswordField(),null);
                btnWard.setFontDimension(20f);
                c.gridy++;

                Panel.add(btnWard, c);
            }

            // Messaggio di benvenuto
            JOptionPane.showMessageDialog(null, "Benevenuto. Scegli il reparto dove vuoi fare una segnalazione!", "info", JOptionPane.INFORMATION_MESSAGE);
        }else{
            // Creazione del model
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Machine","Segnala"},0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column != 0; // se la colonna è diversa da 0 ritorna true (modificabile) altrimenti false (non editabile)
                }
            };

            // For per creare le righe della tabella
            for(int i = 0; i < nMachine ; i++){
                model.addRow(new Object[]{MachineName+(i+1),""});
            }

            // Tabella
            JTable Table = new JTable(model);

            // Pannello di Scorrimento
            JScrollPane scrollPane = new JScrollPane(Table);
            scrollPane.setPreferredSize(new Dimension(450,350));

            c.gridx = 0;
            c.gridy++;

            Panel.add(scrollPane, c);

            // Bottone per Segnalare
            LButton btnReport = new LButton("Segnala", new Color(33, 150, 243), Color.lightGray, this.tfUser, new JPasswordField(), model);

            c.gridwidth = 2;
            c.gridy++;

            Panel.add(btnReport, c);

            // Messaggio di benvenuto
            JOptionPane.showMessageDialog(null, "Benevenuto nel Reparto "+Ward+"\nSe hai delle segnalazioni da fare,\nscrivi affianco al nome della Machine il tipo di segnalazione e clicca Segnala.\nArrivederci!", "info", JOptionPane.INFORMATION_MESSAGE);
        }

        Frame.revalidate();
    }
}
