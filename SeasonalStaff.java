public class SeasonalStaff extends Staff {
    private double hourlyWage;

    public SeasonalStaff(String sID, String sName, double hourlyWage){
        super(sID, sName);
        this.hourlyWage = hourlyWage;
    }

    public double getHourlyWage(){
        return this.hourlyWage;
    }

    public void setHourlyWage(double hourlyWage){
        this.hourlyWage = hourlyWage;
    }

    @Override
    public double paySalary(int workedHours){
        return this.hourlyWage * workedHours;
    }

    @Override
    public String toString(){
        return this.sID +"_" + this.sName +"_"+String.format("%.0f",this.hourlyWage);
    }
}