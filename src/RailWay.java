import java.sql.*;
import java.util.Scanner;

public class RailWay {
    public static void main (String args[]) {
        System.out.println("Welcome to the SEPTA command line tool");
        Scanner scanner = new Scanner(System.in);
        ParameterizedQueries p = new ParameterizedQueries();
        final String url = "jdbc:postgresql://localhost:5432/";
        final String user = "emh128";
        final String password = "cs1555";

        try {
            Connection connection = null;
            PreparedStatement statement = null;
            connection = DriverManager.getConnection(url, user, password);


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

                    } else if(secondChoice == 2 || secondChoice == 1){

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

                }  else if(mainChoice == 2) {
                    System.out.println("1.) Single Route Trip Search\n2.) Combination Route Trip Search\n" +
                            "3.) Add Reservation\n4.) More\n5.) back");
                    int secondChoice = scanner.nextInt();

                    if(secondChoice == 1) {
                        statement = connection.prepareStatement(p.singleRoute);
                    } else if(secondChoice == 2) {
                        //combination
                    } else if(secondChoice == 3) {
                        statement = connection.prepareStatement(p.addReservation);
                    } else if(secondChoice == 4) {
                        System.out.println("1.) Fewest stops\n2.) Run through most stations\n" +
                                "3.) Lowest price\n4.) Highest price\n5.) Least total time\n6.) Most total time\n" +
                                "7.) Least total distance\n8.) Most total distance\n 9.) back");
                        int thirdChoice = scanner.nextInt();
                        if(thirdChoice == 1) {}
                        else if(thirdChoice == 2) {}
                        else if(thirdChoice == 3) {}
                        else if(thirdChoice == 4) {}
                        else if(thirdChoice == 5) {}
                        else if(thirdChoice == 6) {}
                        else if(thirdChoice == 7) {}
                        else if(thirdChoice == 8) {}

                    }

                    System.out.println("\nEnter station 1 id\n");
                    String station1 = scanner.nextLine();
                    statement.setString(1, station1);
                    System.out.println("\nEnter station 2 id\n");
                    String station2 = scanner.nextLine();
                    statement.setString(2, station2);
                    System.out.println("\nEnter weekday id\n");
                    String weekday = scanner.nextLine();
                    statement.setString(3, weekday);
                    ResultSet rs = statement.executeQuery();

                }
                else if(mainChoice == 3) {
                    System.out.println("1.) Find all trains that pass through a specific station at a specific\n" +
                            "day/time combination\n2.) Find the routes that travel more than one rail line\n" +
                            "3.) Find routes that pass through the same stations but don’t have\n" +
                            "the same stops\n4.) Find any stations through which all trains pass through" +
                            "\n5.) Find all the trains that do not stop at a specific station\n6.) Find routes that stop at least at XX% of the Stations they visit" +
                            "\n7.) Display the schedule of a route\n" +
                            "8.) Find the availability of a route at every stop on a specific day and time\n9.) back");
                    int secondChoice = scanner.nextInt();

                    // specificStationDayTime -     station_id, weekday, time
                    // moreThanOneRail -            no params
                    // sameStationDifferentStop -   route_ID
                    // allTrainsPass -              no params
                    // doNotStopAtStation -         station_id
                    // percentStops -               0 >= percentage <= 1
                    // routeSchedule -              route_id
                    // availableDayTime -           route_id, weekday, time


                    if(secondChoice == 1) {
                        statement = connection.prepareStatement(p.specificStationDayTime);
                    }
                    else if(secondChoice == 2) {
                        statement = connection.prepareStatement(p.moreThanOneRail);
                    }
                    else if(secondChoice == 3) {
                        statement = connection.prepareStatement(p.sameStationDifferentStop);
                    }
                    else if(secondChoice == 4) {
                        statement = connection.prepareStatement(p.allTrainsPass);
                    }
                    else if(secondChoice == 5) {
                        statement = connection.prepareStatement(p.doNotStopAtStation);
                    }
                    else if(secondChoice == 6) {
                        statement = connection.prepareStatement(p.percentStops);
                    }
                    else if(secondChoice == 7) {
                        statement = connection.prepareStatement(p.routeSchedule);
                    }
                    else if(secondChoice == 8) {
                        statement = connection.prepareStatement(p.availableDayTime);
                    }


                }
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