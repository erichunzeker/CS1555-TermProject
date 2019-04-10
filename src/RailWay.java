import java.sql.*;
import java.util.Scanner;

public class RailWay {
    public static void main (String args[]) {
        System.out.println("Welcome to the SEPTA command line tool");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("1.) Update customer list\n2.) Find a trip between two stations\n" +
                    "3.) Advanced searches\n4.) Database Administrator Operations\n5.) quit\n");
            int mainChoice = scanner.nextInt();

            if(mainChoice == 1) {
                System.out.println("1.) Add Customer\n2.) Edit Customer\n" +
                        "3.) View Customer\n4.) back");
                int secondChoice = scanner.nextInt();



            }
            else if(mainChoice == 2) {}
            else if(mainChoice == 3) {}
            else if(mainChoice == 4) {}
            else if(mainChoice == 5) {
                System.out.println("Goodbye");
                System.exit(0);
            }
            else
                System.out.println("pick a valid number, asshole");
        }

    }
}