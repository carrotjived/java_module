import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Create a class to save the information from file. This makes calling the data from file easier. 
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

    //Class methods to get the name of each data. 
    public String getName() { return name;}
    public double getRate() { return rate;}
    public double getOverTimeRate() { return overtimeRate;}
    public double getTax() { return tax;}
}




public class salary_calc {

    //Methods

    //Read from file method. Reads the file using Scanner. 
    static List<Person> readFile(String fileName){
        List<Person> people = new ArrayList<>();
        try {Scanner scanner = new Scanner(new File(fileName));
            boolean header = true;

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(",");
                
                //Makes sure to skip the first line of the file to proceed to the real data.
                if (header){
                    header = false;
                    continue;
                }

                String name = values[0];
                double rate = Double.parseDouble(values[1]);
                double overtimeRate = Double.parseDouble(values[2]);
                double tax = Double.parseDouble(values[3]);

                //From the parted data, it will be stored in the Person class.
                
                people.add(new Person(name, rate, overtimeRate, tax));
            }

            //Output in case there is error in reading the file.            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }

        return people;
    }

    
    //This finds the name given by the user in the List of Person Classes. When found, it will output their name, their hourly rate
    // and their overtime rate, and their tax deductions. It will then prompt for the user to provide the hours in basic and overtime. Calculates the basic and overtime and then deducts the tax. 
    static void findAndCalculate(String name, List<Person> people){

        Scanner input = new Scanner(System.in);
      
        for (Person person : people){
            if (person.getName().equalsIgnoreCase(name)){
                System.out.println("\nName: " + person.getName());
                System.out.println("Rate: $" + String.format("%,.2f",person.getRate())); //formats the data to show two decimal places. 
                System.out.println("Over Time Rate: $" + String.format("%,.2f",person.getOverTimeRate())); //formats the data to show two decimal places. 
                System.out.println("Tax Rate: " + String.format("%, .2f", person.getTax()) + "%"); //formats the data to show two decimal places. 

                System.out.print("\nEnter Hours: ");
                double hours = Double.parseDouble((input.nextLine()));
                System.out.print("Enter Overtime Hours: ");
                double overtimeHours = Double.parseDouble(input.nextLine());

                double basePay = (person.getRate() * hours);
                double overTimePay = (person.getOverTimeRate() * overtimeHours);
                double grossIncome = (basePay + overTimePay);
                double netIncome = grossIncome - ((basePay + overTimePay) * (person.getTax()/100));

                System.out.println("\nBase Pay: $" + String.format("%,.2f", basePay)); //Formats the output to have two decimal places.
                System.out.println("Over Time Pay: $" + String.format("%,.2f", overTimePay)); //Formats the output to have two decimal places.
                System.out.println("Gross Income: $" + String.format("%,.2f", grossIncome)); //Formats the output to have two decimal places.
                System.out.println("Net Income: $" + String.format("%,.2f", netIncome)); //Formats the output to have two decimal places.
                
                return;
            }

        
        }

        
    }

    //This method is used to create a record in the file if there is no such name in the file. It will ask for the important parameters to be saved before displaying a prompt that a record is created.
    static Person createRecord(String name){

        Scanner input = new Scanner(System.in);
        boolean stopper = false;
        Person newPerson = null;

        while (!stopper){  
        System.out.print("\nWould you like to create record? Y/N: ");
        String createPrompt = input.nextLine();

            if ("y".equalsIgnoreCase(createPrompt)){
                System.out.print("Enter Rate: ");
                double rate = Double.parseDouble(input.nextLine()); //Parses the input to a double data type.

                System.out.print("Enter Over Time Rate: ");           
                double overTimeRate = Double.parseDouble(input.nextLine()); //Parses the input to a double data type.

                System.out.print("Enter Tax Rate: ");
                double tax = Double.parseDouble(input.nextLine()); //Parses the input to a double data type.


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
        List<Person> people = readFile("employee.csv"); //Call the readFile method to open the text file. 

    
        //A while loop in case the user wants to know the salary of more person. 
        while (true){
            System.out.print("\nEnter name (or type 'quit' to exit): ");
            String name = employeeInput.nextLine();

            if ("quit".equalsIgnoreCase(name)){ //If the user prompts 'no' it will stop the whole program with a prompt. 
                System.out.println("Exiting program. Goodbye!");
                break;
            }

            boolean found = false;

            //Loop to see if the input is in the file.
            for (Person person : people){
                if (person.getName().equalsIgnoreCase(name)){ //equals.IgnoreCase ignores the case of both data and input to make sure that comparison is better. 
                    findAndCalculate(name, people); //Calls the findAndCalculate method if it is true.
                    found = true;
                    break;
                }
            }

            if (!found){ 
                System.out.println("\nNo record found " + name + ".");
                Person newPerson = createRecord(name); //Calls the createRecord to ask if the new name needs a record. 

                if (newPerson != null){
                    people.add(newPerson);
                    findAndCalculate(name, people);
                }

                
            }

        }

    }


}


