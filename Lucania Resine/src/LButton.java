import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Vector;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class LButton extends JButton implements ActionListener {
    // Variabili
    private LTextField tfUser;
    private JPasswordField JfPassword = new JPasswordField();
    private DefaultTableModel model;
    private JTable table;

    // Costruttore
    public LButton(String Text,Color textColor,Color textBackground,LTextField tfUser, JPasswordField JfPassword, DefaultTableModel model, JTable table){
        super(Text);
        this.tfUser = tfUser;
        this.JfPassword= JfPassword;
        this.model = model;
        this.table = table;
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
        else if(getText().equals("Elenco Segnalazioni")) ReportFrame();
        else if(getText().equals("Corrugato")) showCorrugato();
        else if (getText().equals("Rotazionale")) showRotazionale();
        else if(getText().equals("PVC")) showPVC();
        else if(getText().equals("Polietilene")) showPolietilene();
        else if(getText().equals("Aggiorna")) UpdateFile();
        else if(getText().equals("Profilo")) showProfile();
    }

    // Funzione per cambiare la dimensione del Font 
    public void setFontDimension(float dimension){
        setFont(getFont().deriveFont(dimension));
    }

    // Funzione per confrontare le credenziali inserite con quelle nel file
    private void checkCredentials() {
        String AccountPath = "File/Accounts.csv";

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
                V_Reports.add(new Report(machine.toString() ,value.toString(),tfUser.getText()));
            }
        }

        // Controllo se ci sono elementi nel Vector
        if(V_Reports.size() > 0){
            // Codice per scrivere le segnalazioni sul file
            try (PrintWriter writer = new PrintWriter(new FileWriter("File/Segnalazioni.csv", true))) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Formato data e ora

                // for each per scrivere ogni segnalazione nel file con la data e l'ora attuali
                for (Report report : V_Reports) {
                    // Controllo se la segnalazione è vuota
                    if(!report.getReport().trim().isEmpty()){
                        String DateTime = dtf.format(LocalDateTime.now()); // Ottieni la data e l'ora attuali
                        writer.println(report.getReporter() +";"+ report.getMachine() +";"+ report.getReport() + ";" + DateTime +";No"); // Scrivi la segnalazione seguita dalla data e ora nel file
                    } else {
                        JOptionPane.showMessageDialog(null, "Fai Attenzione, la segnalazione non può essere vuota.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        return;
                    }                    
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

    // Funzione per la visualizzazioen della tabella delle segnalazioni
    private void ReportFrame(){
        LFrame Frame = new LFrame("Elenco Segnalazioni", new ImageIcon("img/logoAzienda.jpg"), 800, 600, true);

        // Contenitore Intermedio
        Container contentPane = Frame.getContentPane();

        // Pannello
        JPanel Panel = new JPanel();

        // Layout con GridBagConstraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 5, 5);
        
        Panel.setLayout(new GridBagLayout());
        contentPane.add(Panel);

        LLabel lblTitolo = new LLabel("Elenco Segnalazioni", null);
        lblTitolo.setFontDimension(25f);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        Panel.add(lblTitolo, c);

        // Tabella delle Segnalazioni
        DefaultTableModel Rmodel = new DefaultTableModel(new Object[]{"Segnalatore", "Macchina", "Segnalazione", "Data e Ora", "Risolto"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Rendi non modificabili le celle fino a "Data e Ora" e la cella "Risolto"
                return column > -1;
            }
        };
        
        JTable table = new JTable(Rmodel);
        
        JScrollPane scrollPane = new JScrollPane(table);

        c.gridx++;
        c.gridy++;
        c.gridwidth = 1;

        Panel.add(scrollPane, c);

        // Bottone per aggiornare lo stato delle segnalazioni
        LButton btnUpdate = new LButton("Aggiorna", new Color(33, 150, 243), Color.lightGray, this.tfUser, new JPasswordField(), Rmodel, table);

        c.gridwidth = 2;
        c.gridy++;

        Panel.add(btnUpdate, c);
        
        // Messaggio di benvenuto
        JOptionPane.showMessageDialog(null, "Benevenuto nella visualizzazione dell'elenco\ndelle segnalazioni. Se vuoi cambiare lo stato di una delle\nsegnalazioni seleziona una riga e clicca sul pulsante aggiorna.\nArrivederci!", "info", JOptionPane.INFORMATION_MESSAGE);

        // Promemoria
        JOptionPane.showMessageDialog(null, "Ricorda non puoi cambiare lo stato\ndi una segnalzione da Si a No.\n", "Attenzione", JOptionPane.WARNING_MESSAGE);

        // Carica i dati dalla tabella
        loadTableData(Rmodel);
    }

    private void loadTableData(DefaultTableModel Rmodel) {
        String filePath = "File/Segnalazioni.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Iteraizione per riempire la tabella con i dati nel file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if(!data[0].equals("Segnalatore")) Rmodel.addRow(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateFile() {
        int selectedRow = table.getSelectedRow(); // Ottieni l'indice della riga selezionata dalla tabella
        boolean updated = false; // Variabile per controllare se il file è stato aggiornato con successo
    
        // Controlla se la riga selezionata è valida
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            String risoltoValue = model.getValueAt(selectedRow, 4).toString();
            // Controlla se il valore nella colonna Risolto è a No
    
            if (risoltoValue.equals("No")) {
                // Aggiorna il valore nella tabella
                model.setValueAt("Si", selectedRow, 4);
                updated = true;
    
                try {
                    // Leggi il file originale
                    File inputFile = new File("File/Segnalazioni.csv");
                    Scanner scanner = new Scanner(inputFile);
    
                    // Crea un nuovo file temporaneo per memorizzare le modifiche
                    File tempFile = new File("File/Segnalazioni_temp.csv");
                    PrintWriter writer = new PrintWriter(tempFile);
    
                    // Aggiorna il file temporaneo con le modifiche
                    int lineNum = 0;
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (lineNum == selectedRow) {
                            // Aggiorna la riga corrente con "Si" nella colonna "Risolto"
                            line = line.substring(0, line.lastIndexOf(";")) + ";Si";
                        }
                        writer.println(line);
                        lineNum++;
                    }
    
                    scanner.close();
                    writer.close();
    
                    // Sostituisci il file originale con il file temporaneo
                    inputFile.delete();
                    tempFile.renameTo(inputFile);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'aggiornamento del file!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "Non hai selezionato nulla!", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    
        if (updated) JOptionPane.showMessageDialog(null, "File aggiornato con successo!", "info", JOptionPane.INFORMATION_MESSAGE);
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
        LFrame Frame = new LFrame(Ward,new ImageIcon("img/logoAzienda.jpg"), 600, 600,true);
        
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
            String[] ButtonWard = {"Corrugato", "PVC", "Rotazionale", "Polietilene", "Elenco Segnalazioni", "Profilo"};

            for(String Department: ButtonWard){
                LButton btnWard = new LButton(Department, new Color(33, 150, 243), Color.lightGray, this.tfUser, this.JfPassword,null,null);
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
            LButton btnReport = new LButton("Segnala", new Color(33, 150, 243), Color.lightGray, this.tfUser, new JPasswordField(), model, Table);

            c.gridy++;

            Panel.add(btnReport, c);

            // Bottone per Visualizzare il proprio file
            LButton btnProfile = new LButton("Profilo", new Color(33, 150, 243), Color.lightGray, this.tfUser, this.JfPassword, model, Table);
            
            c.gridy++;

            Panel.add(btnProfile, c);

            // Messaggio di benvenuto
            JOptionPane.showMessageDialog(null, "Benevenuto nel Reparto "+Ward+"\nSe hai delle segnalazioni da fare,\nscrivi affianco al nome della Machine il tipo di segnalazione e clicca Segnala.\nArrivederci!", "info", JOptionPane.INFORMATION_MESSAGE);
        }

        Frame.revalidate();
    }

    private void showProfile(){
        // Frame
        LFrame Frame = new LFrame("Profilo", new ImageIcon("img/logoAzienda.jpg"), 600, 600, true);
    
        // Contenitore Intermedio
        Container contentPane = Frame.getContentPane();
    
        // Pannello
        JPanel Panel = new JPanel();
    
        // Layout 
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 5, 5);
        
        Panel.setLayout(new GridBagLayout());
        contentPane.add(Panel);

        // titolo
        LLabel lblTitolo = new LLabel("Profilo", null);
        lblTitolo.setFontDimension(25f);

        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
    
        Panel.add(lblTitolo,c);

        // Immagine dell' Account
        LLabel ImgAccount = new LLabel(null, new ImageIcon("img/Account.png"));
    
        c.gridy++;
        c.gridwidth = 1;
    
        Panel.add(ImgAccount,c);
    
        // Username
        JTextField Username = new JTextField("Username: "+this.tfUser.getText(), 20);
        Username.setEditable(false);
        c.gridy++;
        Panel.add(Username, c);
    
        // Password
        JPasswordField Password = new JPasswordField(20);
        char[] CharPass = JfPassword.getPassword();
        String StrPass = new String(CharPass);
        Password.setEchoChar((char) 0);
        Password.setText("Password: "+ StrPass);
        Password.setEditable(false);
        c.gridy++;
        Panel.add(Password, c);
    
        // Ruolo e Reparto
        String username = this.tfUser.getText();
        String role = "";
        String department = "";

        try {
            File file = new File("File/Accounts.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                // Controllo se l'username della segnalazione è uguale all'user corrente 
                if (parts.length >= 3 && parts[0].equals(username)) {
                    role = parts[2];
                    if(parts[3].equals("No")) department = "Ø";
                    else department = parts[3];
                    break;
                }
            }
    
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ruolo
        JTextField RoleField = new JTextField("Ruolo: " + role, 20);
        RoleField.setEditable(false);
        c.gridy++;
        Panel.add(RoleField, c);

        // Reparto
        JTextField DepartmentField = new JTextField("Reparto: " + department, 20);
        DepartmentField.setEditable(false);
        c.gridy++;
        Panel.add(DepartmentField, c);

        // Tabella per la visualizzazione dello stato delle segnalazioni
        DefaultTableModel signalModel = new DefaultTableModel(new Object[]{"Segnalazione", "Stato"}, 0);
        
        JTable signalTable = new JTable(signalModel);

        JScrollPane scrollPane = new JScrollPane(signalTable);
        scrollPane.setPreferredSize(new Dimension(500, 200)); 
        c.gridy++;
        Panel.add(scrollPane, c);
        
        // Messaggio di benvenuto
        JOptionPane.showMessageDialog(null, "Benevenuto nel tuo Profilo\nqui potrai vedere le tue informazioni generali\ne lo stato delle tue segnalazioni.\nArrivederci!", "info", JOptionPane.INFORMATION_MESSAGE);

        updateSignalTable(signalModel);
        
        Frame.revalidate();
    }

    private void updateSignalTable(DefaultTableModel signalModel) {
        String username = this.tfUser.getText();
    
        try {
            File file = new File("File/Segnalazioni.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
    
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4 && parts[0].equals(username)) {
                    String segnalazione = parts[2];
                    // Operatore ternario
                    // Controllo se la segnalazione è stata risolta 
                    String stato = parts[4].equals("Si") ? "Completata" : "In corso";
                    signalModel.addRow(new Object[]{segnalazione, stato});
                }
            }
    
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
