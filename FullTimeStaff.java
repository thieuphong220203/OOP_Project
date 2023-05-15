public class FullTimeStaff extends Staff {
    protected double baseSalary;
    protected double bonusRate;

    public FullTimeStaff(String sID, String sName,double baseSalary, double bonusRate){
        super(sID, sName);
        this.baseSalary = baseSalary;
        this.bonusRate = bonusRate;
    }

    public double getBaseSalary(){
        return this.baseSalary;
    }

    public void setBaseSalary(double baseSalary){
        this.baseSalary = baseSalary;
    }

    public double getBonusRate(){
        return this.bonusRate;
    }

    public void setBonusRate(double bonusRate){
        this.bonusRate = bonusRate;
    }

    @Override 
    public double paySalary(int workedDays){
        return this.baseSalary * this.bonusRate + 100000 * (workedDays <= 21 ? 0 : (workedDays - 21)) ;
    }

    @Override
    public String toString(){
        return this.sID +"_"+this.sName+"_"+this.bonusRate+ "_" +String.format("%.0f",this.baseSalary);
    }
}
