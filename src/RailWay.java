import java.sql.*;
import java.util.Scanner;

public class RailWay {
    public static void main (String args[]) {
        System.out.println("Welcome to the SEPTA command line tool");
        Scanner scanner = new Scanner(System.in);
        ParameterizedQueries p = new ParameterizedQueries();

        System.out.println(p.addCustomer);
        try {
            Connection connection = null;
            PreparedStatement statement = null;

            while(true) {
                System.out.println("1.) Update customer list\n2.) Find a trip between two stations\n" +
                        "3.) Advanced searches\n4.) Database Administrator Operations\n5.) quit\n");
                int mainChoice = scanner.nextInt();

                if(mainChoice == 1) {
                    System.out.println("1.) Add Customer\n2.) Edit Customer\n" +
                            "3.) View Customer\n4.) back");
                    int secondChoice = scanner.nextInt();

                    if(secondChoice == 3) {
                        statement = connection.prepareStatement(p.viewCustomer);
                        System.out.println("\nEnter passenger id\n");
                        String pID = scanner.nextLine();
                        statement.setString(1, pID);
                        ResultSet rs = statement.executeQuery();

                    } else {

                        if(secondChoice == 1)
                            statement = connection.prepareStatement(p.addCustomer);
                        else {
                            statement = connection.prepareStatement(p.editCustomer);
                            System.out.println("Enter passenger ID to edit: \n");
                            String pID = scanner.nextLine();
                            statement.setString(10, pID);
                        }

                        //firstname, lastname, phone, street, city, state, zip, country, email


                        String firstname, lastname, phone, street, city, state, zip, country, email;

                        System.out.println("Enter first name: \n");
                        firstname = scanner.nextLine();
                        statement.setString(1, firstname);

                        System.out.println("\nEnter last name\n");
                        lastname = scanner.nextLine();
                        statement.setString(2, lastname);

                        System.out.println("\nEnter phone number\n");
                        phone = scanner.nextLine();
                        statement.setString(3, phone);

                        System.out.println("\nEnter street address\n");
                        street = scanner.nextLine();
                        statement.setString(4, street);

                        System.out.println("\nEnter city\n");
                        city = scanner.nextLine();
                        statement.setString(5, city);

                        System.out.println("\nEnter state\n");
                        state = scanner.nextLine();
                        statement.setString(6, state);

                        System.out.println("\nEnter zip code\n");
                        zip = scanner.nextLine();
                        statement.setString(7, zip);

                        System.out.println("\nEnter country\n");
                        country = scanner.nextLine();
                        statement.setString(8, country);

                        System.out.println("\nEnter email\n");
                        email = scanner.nextLine();
                        statement.setString(9, email);

                        ResultSet rs = statement.executeQuery();
                    }

                }
                else if(mainChoice == 2) {}
                else if(mainChoice == 3) {}
                else if(mainChoice == 4) {
                    System.out.println("1.) Import Database\n2.) Export Database\n" +
                            "3.) Delete Database\n4.) back");
                    int secondChoice = scanner.nextInt();
                    if(secondChoice == 1) {
                        //run phase1.sql then data file
                    } else if(secondChoice == 2) {
                        // idk
                    } else if(secondChoice == 3) {
                        //drop all tables
                    }
                }
                else if(mainChoice == 5) {
                    System.out.println("Goodbye");
                    System.exit(0);
                }
                else
                    System.out.println("pick a valid number, asshole");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}