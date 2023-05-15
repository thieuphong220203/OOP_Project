import java.io.*;
import java.text.CollationElementIterator;
import java.util.*;

import javax.sound.sampled.SourceDataLine;

public class StoreManagement {
    private ArrayList<Staff> staffs;
    private ArrayList<String> workingTime;
    private ArrayList<Invoice> invoices;
    private ArrayList<InvoiceDetails> invoiceDetails;
    private ArrayList<Drink> drinks;

    public StoreManagement(String staffPath, String workingTimePath, String invoicesPath, String detailsPath, String drinksPath) {
        this.staffs = loadStaffs(staffPath);
        this.workingTime = loadFile(workingTimePath);
        this.invoices = loadInvoices(invoicesPath);
        this.invoiceDetails = loadInvoicesDetails(detailsPath);
        this.drinks = loadDrinks(drinksPath);
    }

    public ArrayList<Staff> getStaffs() {
        return this.staffs;
    }

    public void setStaffs(ArrayList<Staff> staffs){
        this.staffs = staffs;
    }
    
    public ArrayList<Drink> loadDrinks(String filePath) {
        ArrayList<Drink> drinksResult = new ArrayList<Drink>();
        ArrayList<String> drinks = loadFile(filePath);

        for (String drink : drinks) {
            String[] information = drink.split(",");
            drinksResult.add(new Drink(information[0], Integer.parseInt(information[1])));
        }

        return drinksResult;
    }

    public ArrayList<Invoice> loadInvoices(String filePath) {
        ArrayList<Invoice> invoiceResult = new ArrayList<Invoice>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new Invoice(information[0], information[1], information[2]));
        }

        return invoiceResult;
    }

    public ArrayList<InvoiceDetails> loadInvoicesDetails(String filePath) {
        ArrayList<InvoiceDetails> invoiceResult = new ArrayList<InvoiceDetails>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new InvoiceDetails(information[0], information[1], Integer.parseInt(information[2])));
        }

        return invoiceResult;
    }

    // requirement 1
    public ArrayList<Staff> loadStaffs(String filePath) {
        //code here and modify the return value
        ArrayList<Staff> listStaff = new ArrayList<Staff>(100);
        ArrayList<String> temp = loadFile(filePath);

        for(String s : temp) {
            String[] t = s.split(",");
            if(t.length == 4) {
                listStaff.add(new FullTimeStaff(t[0],t[1] ,Double.parseDouble(t[2]) , Double.parseDouble(t[3])));
            } else if(t.length == 5) {
                listStaff.add(new Manager(t[0], t[1], Double.parseDouble(t[2]), Double.parseDouble(t[3]), Double.parseDouble(t[4])));
            }else if(t.length == 3){
                listStaff.add(new SeasonalStaff(t[0], t[1], Double.parseDouble(t[2])));
            }
        }
        return listStaff;
    }
    
    // requirement 2
    public ArrayList<SeasonalStaff> getTopFiveSeasonalStaffsHighSalary() {
        //code here and modify the return value
        ArrayList<SeasonalStaff> list = new ArrayList<SeasonalStaff>(100);
        for(Staff s : staffs){
            if(s instanceof SeasonalStaff){
                list.add((SeasonalStaff)s);
            }
        }

        //list time and seasonal_id of staff
        ArrayList<Integer> listTime = new ArrayList<Integer>(100);
        ArrayList<String> Seasonal_id = new ArrayList<String>(100);
        int i = 0;
        for(String s : workingTime){
            String[] a = s.split(",");
        
            if(a[0].contains("TV")){
                Seasonal_id.add(a[0]);
                listTime.add(Integer.parseInt(a[1]));
                i++;
            }
        }
        
        for( i = list.size() - 1; i >=0 ; i--) {
            for(int j = 0; j  < i; j++){
                if((list.get(j).paySalary(getTime(list.get(j), Seasonal_id, listTime)) < list.get(j+1).paySalary(getTime(list.get(j+1), Seasonal_id, listTime)))){
                    Collections.swap(list, j+1, j);
                }
            }
        }
        
        ArrayList<SeasonalStaff> topFive = new ArrayList<SeasonalStaff>(5);
        for(i = 0 ; i < 5; i++){
            topFive.add(list.get(i));
        }
        return topFive ;
    }

    private Integer getTime(Staff s, ArrayList<String>id, ArrayList<Integer>time){
        for(int i = 0; i < id.size(); i++){
            if(s.sID.equals(id.get(i))){
                return time.get(i);
            }
        }
        return null;
    }

    // requirement 3
    public ArrayList<FullTimeStaff> getFullTimeStaffsHaveSalaryGreaterThan(int lowerBound) {
        //code here and modify the return value
        ArrayList<FullTimeStaff> fullTimeStaffsList = new ArrayList<FullTimeStaff>(100);
        ArrayList<FullTimeStaff> fullTimeStaffsListHigher = new ArrayList<FullTimeStaff>(100);
        
        ArrayList<String> fullTimeStaffId = new ArrayList<String>(100);
        ArrayList<Integer> ListTime = new ArrayList<Integer>(100);
        ArrayList<Double> ListSalary = new ArrayList<Double>(100);

        //get all FullTimeStaff
        for(Staff s : staffs){
            if(s instanceof FullTimeStaff){
                fullTimeStaffsList.add((FullTimeStaff)s);
            }
        }

        for(String s : workingTime){
            String[] temp = s.split(",");
            if(s.contains("CT") || s.contains("QL")){
                fullTimeStaffId.add(temp[0]);
                ListTime.add(Integer.parseInt(temp[1]));
            }
        }

        for(FullTimeStaff staff : fullTimeStaffsList){
            Double s = staff.paySalary(getTime(staff, fullTimeStaffId, ListTime));
            ListSalary.add(s);
        }

        for(int i = 0; i < fullTimeStaffsList.size();i++){
            if(ListSalary.get(i) > lowerBound){
                fullTimeStaffsListHigher.add(fullTimeStaffsList.get(i));
            }
        }
        return fullTimeStaffsListHigher;
    }

    // requirement 4
    public double totalInQuarter(int quarter) {
        double total = 0;
        // code here
        ArrayList<Invoice> quarter1 = new ArrayList<Invoice>(100);
        ArrayList<Invoice> quarter2 = new ArrayList<Invoice>(100);
        ArrayList<Invoice> quarter3 = new ArrayList<Invoice>(100);
        ArrayList<Invoice> quarter4 = new ArrayList<Invoice>(100);
        
        for(Invoice s : invoices){
            String[] temp = s.getDate().split("/");
            if(temp[1].equals("01") || temp[1].equals("02") 
                || temp[1].equals("03")){
                quarter1.add(s);
            }else if(temp[1].equals("04") || temp[1].equals("05") || 
            temp[1].equals("06")) {
                quarter2.add(s);
            }else if(temp[1].equals("07") || temp[1].equals("08") || 
            temp[1].equals("09")){
                quarter3.add(s);
            }else {
                quarter4.add(s);
            }
        }

        if(quarter == 1){
            total = SumOfMoney(quarter1);
        }else if(quarter == 2){
           total = SumOfMoney(quarter2);
        }else if(quarter == 3){
            total = SumOfMoney(quarter3);
        }else if(quarter == 4){
            total = SumOfMoney(quarter4);
        }
        
        return total;
    }

    private int SumOfMoney(ArrayList<Invoice> list){
        int total = 0;
        for(int i = 0; i < list.size();i++){
            String temp1 = list.get(i).getInvoiceID();
            for(int j = 0; j < invoiceDetails.size();j++){
                if(temp1.equals(invoiceDetails.get(j).getInvoiceID())){
                    for(int k =0; k < drinks.size();k++){
                        if(invoiceDetails.get(j).getDName().toLowerCase().equals(drinks.get(k).getdName().toLowerCase())){
                            total = total + drinks.get(k).getPrice() * invoiceDetails.get(j).getAmount();
                        }
                    }
                }
            }
        }
        return total;
    }
    // requirement 5
    public Staff getStaffHighestBillInMonth(int month) {
        Staff maxStaff = null;
        //code here
        ArrayList<String> IdOfInvoiceList = new ArrayList<String>(100); //List Id of invoices match with month given
        ArrayList<String> IdOfStaffList = new ArrayList<String>(100); // list Id of Staffs match with month given
        ArrayList<Integer> quantitiesList = new ArrayList<Integer>(100); //list of Quantities match with Id of invoices match with month given
        ArrayList<Integer> MonthList = new ArrayList<Integer>(100); // month list
        
        //List Month 
        for(Invoice s : invoices){
            String[] temp = s.getDate().split("/");
            MonthList.add(Integer.parseInt(temp[1]));
        }

        //Find staff who have invoice in month given
        for(int i = 0; i < invoices.size();i++){
            if(MonthList.get(i) == month){
                IdOfInvoiceList.add(invoices.get(i).getInvoiceID());
                IdOfStaffList.add(invoices.get(i).getStaffID());
                // quantitiesList.add(invoices.get(i).);
            }
        }

        

        
        //sort ID of invoice and Staff with month given
        for(int i = IdOfInvoiceList.size() - 1; i >=0; i--){
            for(int j = 0; j <i ;j++){
                if(IdOfInvoiceList.get(j).compareTo(IdOfInvoiceList.get(j+1)) > 0){
                    Collections.swap(IdOfInvoiceList,j,j+1);
                    Collections.swap(IdOfStaffList,j,j+1);
                }
            }
        }

        //compare IdOfInvoice with IdOfInvoiceDetails to get quantities
        for(int i = 0; i < IdOfInvoiceList.size();i++){
            int sum = 0;
            for(int j = 0; j < invoiceDetails.size();j++){
                if(IdOfInvoiceList.get(i).equals(invoiceDetails.get(j).getInvoiceID())) {
                    sum = sum + invoiceDetails.get(j).getAmount();
                }
            }
            quantitiesList.add(sum);
        }

        //get quantities of each ID invoice
        for(int i = quantitiesList.size() - 1; i >=0; i--){
            for(int j = 0; j < i; j++){
                if(quantitiesList.get(j) < quantitiesList.get(j+1)){
                    Collections.swap(quantitiesList,j,j+1);
                    Collections.swap(IdOfStaffList,j,j+1);
                }
            }
        }

        //get staff has highest bill in month
        for(Staff s : staffs){
            if(s.sID.equals(IdOfStaffList.get(0))){
                maxStaff = s;
                break;
            }
        }
        return maxStaff;
    }

    // load file as list
    public static ArrayList<String> loadFile(String filePath) {
        String data = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader fReader = new BufferedReader(reader);

            while ((data = fReader.readLine()) != null) {
                list.add(data);
            }

            fReader.close();
            reader.close();

        } catch (Exception e) {
            System.out.println("Cannot load file");
        }
        return list;
    }

    public void displayStaffs() {
        for (Staff staff : this.staffs) {
            System.out.println(staff);
        }
    }

    public <E> boolean writeFile(String path, ArrayList<E> list) {
        try {
            FileWriter writer = new FileWriter(path);
            for (E tmp : list) {
                writer.write(tmp.toString());
                writer.write("\r\n");
            }

            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }

    public <E> boolean writeFile(String path, E object) {
        try {
            FileWriter writer = new FileWriter(path);

            writer.write(object.toString());
            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }
}