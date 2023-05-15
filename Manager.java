public class Manager extends FullTimeStaff {
    private double allowance;

    public Manager(String sID, String sName, double baseSalary,double bonusRate, double allowance){
        super(sID, sName,baseSalary,bonusRate);
        this.allowance = allowance;
    }

    public double getAllowance(){
        return this.allowance;
    }

    @Override
    public double paySalary(int workedDays){
        return super.paySalary(workedDays) + this.allowance;
    }

    public String toString(){
        return this.sID +"_"+this.sName+"_"+super.getBonusRate() +"_"+String.format("%.0f",super.getBaseSalary())+"_" + String.format("%.0f",this.allowance);
    }
}
