import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Person {
    private final String name;
    private final double rate;
    private final double overtimeRate;
    private final double tax;

    public Person(String name, double rate, double overtimeRate, double tax){
        this.name = name;
        this.rate = rate;
        this.overtimeRate = overtimeRate;
        this.tax = tax;

    }

    public String getName() { return name;}
    public double getRate() { return rate;}
    public double getOverTimeRate() { return overtimeRate;}
    public double getTax() { return tax;}
}


public class salary_calc {

    //Methods
    static List<Person> readFile(String fileName){
        List<Person> people = new ArrayList<>();
        try {Scanner scanner = new Scanner(new File(fileName));
            boolean header = true;

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(",");

                if (header){
                    header = false;
                    continue;
                }

                String name = values[0];
                double rate = Double.parseDouble(values[1]);
                double overtimeRate = Double.parseDouble(values[2]);
                double tax = Double.parseDouble(values[3]);
                
                people.add(new Person(name, rate, overtimeRate, tax));
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }

        return people;
    }

    

    static void findAndCalculate(String name, List<Person> people){

        Scanner input = new Scanner(System.in);
      
        for (Person person : people){
            if (person.getName().equalsIgnoreCase(name)){
                System.out.println("\nName: " + person.getName());
                System.out.println("Rate: $" + String.format("%,.2f",person.getRate()));
                System.out.println("Over Time Rate: $" + String.format("%,.2f",person.getOverTimeRate()));
                System.out.println("Tax Rate: " + String.format("%, .2f", person.getTax()) + "%");

                System.out.print("\nEnter Hours: ");
                double hours = Double.parseDouble((input.nextLine()));
                System.out.print("Enter Overtime Hours: ");
                double overtimeHours = Double.parseDouble(input.nextLine());

                double basePay = (person.getRate() * hours);
                double overTimePay = (person.getOverTimeRate() * overtimeHours);
                double grossIncome = (basePay + overTimePay);
                double netIncome = grossIncome - ((basePay + overTimePay) * (person.getTax()/100));

                System.out.println("\nBase Pay: $" + String.format("%,.2f", basePay));
                System.out.println("Over Time Pay: $" + String.format("%,.2f", overTimePay));
                System.out.println("Gross Income: $" + String.format("%,.2f", grossIncome));
                System.out.println("Net Income: $" + String.format("%,.2f", netIncome));
                
                return;
            }

        
        }

        
    }

    static Person createRecord(String name){

        Scanner input = new Scanner(System.in);
        boolean stopper = false;
        Person newPerson = null;

        while (!stopper){  
        System.out.print("\nWould you like to create record? Y/N: ");
        String createPrompt = input.nextLine();

            if ("y".equalsIgnoreCase(createPrompt)){
                System.out.print("Enter Rate: ");
                double rate = Double.parseDouble(input.nextLine());

                System.out.print("Enter Over Time Rate: ");           
                double overTimeRate = Double.parseDouble(input.nextLine());

                System.out.print("Enter Tax Rate: ");
                double tax = Double.parseDouble(input.nextLine());


                try (FileWriter writer = new FileWriter("employee.csv",true)){
                    writer.write(name + "," + rate + "," + overTimeRate + "," + tax + "\n");

                } catch (IOException e) {System.out.println("Error: Could not save record. Please try again.");
                }

                newPerson = new Person(name, rate, overTimeRate, tax);
                System.out.println("Record Created!\n\n");

                stopper = true;

                }

            else if ("n".equalsIgnoreCase(createPrompt)) {
                System.out.println("No record created.");
                stopper = true;
                }

            else {
                    System.out.println("\nInvalid choice. Please enter Y or N.");

                }
            }

            return newPerson;

    }

    public static void main(String[] args) {


        System.out.println("Employee Payout Program");


        Scanner employeeInput = new Scanner(System.in);
        List<Person> people = readFile("employee.csv");

    

        while (true){
            System.out.print("\nEnter name (or type 'quit' to exit): ");
            String name = employeeInput.nextLine();

            if ("quit".equalsIgnoreCase(name)){
                System.out.println("Exiting program. Goodbye!");
                break;
            }

            boolean found = false;
        for (Person person : people){
            if (person.getName().equalsIgnoreCase(name)){
                findAndCalculate(name, people);
                found = true;
                break;
            }
        }

        if (!found){
            System.out.println("\nNo record found " + name + ".");
            Person newPerson = createRecord(name);

            if (newPerson != null){
                people.add(newPerson);
                findAndCalculate(name, people);
            }

            
        }

        }

    }


}


