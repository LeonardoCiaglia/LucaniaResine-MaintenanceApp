class Report{
    //Variabili
    private String Machine;
    private String Report;
    private String Reporter; 

    //Costruttore
    public Report(String Machine, String Report, String Reporter){
        this.Machine = Machine;
        this.Report = Report;
        this.Reporter = Reporter;
    }

    public String getMachine(){
        return this.Machine;
    }

    public String getReport(){
        return this.Report;
    }

    public String getReporter(){
        return this.Reporter;
    }
}