class Report{
    //Variabili
    private String Machine;
    private String Report;

    //Costruttore
    public Report(String Machine, String Report){
        this.Machine = Machine;
        this.Report = Report;
    }

    public String getMachine(){
        return this.Machine;
    }

    public String getReport(){
        return this.Report;
    }
}