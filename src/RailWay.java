import java.nio.DoubleBuffer;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.Map.Entry;


public class RailWay {

    static String[] tables = {"passenger", "railline", "station", "train", "stop", "route", "route_stop", "schedule", "railline_route", "station_railline"};
	
	static Scanner scanner;
	static ParameterizedQueries p;
	public static Connection connection;

    public static void main (String args[]) {
        System.out.println("Welcome to the SEPTA command line tool");
        final String url = "jdbc:postgresql://localhost:5432/bjc76";
	    final String user = "bjc76";
	    final String password = "password";
        scanner = new Scanner(System.in);
        p = new ParameterizedQueries();
        PreparedStatement statement;
        

        try {
            connection = DriverManager.getConnection(url, user, password);


            while(true) {
                System.out.println("1.) Update customer list\n2.) Find a trip between two stations\n" +
                        "3.) Advanced searches\n4.) Database Administrator Operations\n5.) quit\n");
                int secondChoice, mainChoice = scanner.nextInt();
                scanner.nextLine();
                switch(mainChoice){
                	case 1: 
                		System.out.println("1.) Add Customer\n2.) Edit Customer\n" +
                            "3.) View Customer\n4.) back");
                    	secondChoice = scanner.nextInt();
                    	scanner.nextLine();
                		customerSubMenu(secondChoice);
                		break;
                	case 2:	
                		System.out.println("1.) Single Route Trip Search\n2.) Combination Route Trip Search\n" +
                            "3.) Add Reservation\n4.) Back");
                    	secondChoice = scanner.nextInt();
                    	scanner.nextLine();
                		findTripSubMenu(secondChoice);
                		break;
                	case 3:	
                		System.out.println("1.) Find all trains that pass through a specific station at a specific\n" +
                            "day/time combination\n2.) Find the routes that travel more than one rail line\n" +
                            "3.) Find routes that pass through the same stations but don’t have\n" +
                            "the same stops\n4.) Find any stations through which all trains pass through" +
                            "\n5.) Find all the trains that do not stop at a specific station\n6.) Find routes that stop at least at XX% of the Stations they visit" +
                            "\n7.) Display the schedule of a route\n" +
                            "8.) Find the availability of a route at every stop on a specific day and time\n9.) back");
                    	secondChoice = scanner.nextInt();
                    	scanner.nextLine();
                		advancedSearchesSubMenu(secondChoice);
                		break;
                	case 4:	
                		System.out.println("1.) Import Database\n2.) Export Database\n" +
                            "3.) Delete Database\n4.) back");
                    	secondChoice = scanner.nextInt();
                    	scanner.nextLine();
                		dbaSubMenu(secondChoice);
                		break;
                	case 5:	
                		System.out.println("Goodbye");
                    	System.exit(0);
                    	break;
                    default:
                    	System.out.println("Pick a valid number");
                    	break;
                }                    
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void customerSubMenu(int selection){
	    try{
	    	PreparedStatement statement;
	    	int pID;
	    	switch (selection){
	    		case 1: //ADD CUSTOMER
	    			statement = connection.prepareStatement(p.addCustomer);
	    			updateOrAddCustomer(statement);
	    			break;
	    		case 2: //EDIT CUSTOMER
	    			statement = connection.prepareStatement(p.editCustomer);
	                System.out.println("Enter passenger ID to edit: ");
	                pID = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(10, pID);
	                updateOrAddCustomer(statement);
	    			break;
	    		case 3: //VIEW CUSTOMER
	    			statement = connection.prepareStatement(p.viewCustomer);
	                System.out.println("Enter passenger id");
	                pID = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(1, pID);
	                ResultSet rs = statement.executeQuery();
	                printResults(rs);
	    			break;
	    		case 4: //BACK
	    			return;
	    		default: //INVALID INPUT
	    			System.out.println("Pick a valid number\n");
		   			System.out.println("1.) Add Customer\n2.) Edit Customer\n" +
	                    "3.) View Customer\n4.) back");
	            	int choice = scanner.nextInt();
	            	scanner.nextLine();
	        		customerSubMenu(choice);
	        		return;
	    	}
    	} catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateOrAddCustomer(PreparedStatement statement){
    	try{
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

	        printResults(rs);
	    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findTripSubMenu(int selection){
    	try{
	    	PreparedStatement statement;
	    	String weekday;
	    	int thirdChoice;
	    	//NEXT LINE NOT NEEDED
	    	//statement = connection.prepareStatement(p.singleRoute);
	    	switch (selection){
	    		case 1: //SINGLE ROUTE TRIP SEARCH
	    			/*statement = connection.prepareStatement(p.singleRoute);
					System.out.println("Enter weekday");
					weekday = scanner.nextLine();
					statement.setString(3, weekday);
	    			break;*/
					System.out.println("0.) No sort\n1.) Fewest stops\n2.) Run through most stations\n" +
							"3.) Lowest price\n4.) Highest price\n5.) Least total time\n6.) Most total time\n" +
							"7.) Least total distance\n8.) Most total distance\n9.) back");
					thirdChoice = scanner.nextInt();
					scanner.nextLine();
					if(thirdChoice == 9)
						return;
					findSingleRouteTrips(thirdChoice);
					return;
	    		case 2: 
	    			System.out.println("0.) No sort\n1.) Fewest stops\n2.) Run through most stations\n" +
							"3.) Lowest price\n4.) Highest price\n5.) Least total time\n6.) Most total time\n" +
							"7.) Least total distance\n8.) Most total distance\n9.) back");
					thirdChoice = scanner.nextInt();
					scanner.nextLine();
					if(thirdChoice == 9)
						return;
					findCombinationTrips(thirdChoice);
	    			//combination
	    			break;
	    		case 3: //ADD RESERVATION
	    			statement = connection.prepareStatement(p.addReservation);

					System.out.println("Enter weekday");
					weekday = scanner.nextLine();
					statement.setString(1, weekday);

					System.out.println("Enter time (xx:xx:xx)");
					String time = scanner.nextLine();
					Time t = Time.valueOf(time);
					statement.setTime(2, t);

					System.out.println("Enter route id");
					int route_id = scanner.nextInt();
					scanner.nextLine();
					statement.setInt(3, route_id);

					ResultSet rs = statement.executeQuery();

					printResults(rs);

					return;
	    		/*case 4: //MORE
	    			System.out.println("1.) Fewest stops\n2.) Run through most stations\n" +
	                    "3.) Lowest price\n4.) Highest price\n5.) Least total time\n6.) Most total time\n" +
	                    "7.) Least total distance\n8.) Most total distance\n 9.) back");
					int thirdChoice = scanner.nextInt();
					findTripsSubMenuExtra(thirdChoice);
	    			break;*/
	    		case 4: //BACK
	    			return;
	    		default: //INVALID INPUT
	    			System.out.println("Pick a valid number\n");
	    			System.out.println("1.) Single Route Trip Search\n2.) Combination Route Trip Search\n" +
	                	"3.) Add Reservation\n4.) More\n5.) back");
	            	int choice = scanner.nextInt();
	            	scanner.nextLine();
	        		findTripSubMenu(choice);
	        		return;
	    	}
	    	/*System.out.println("Enter station 1 id");
	        int station1 = scanner.nextInt();
	        scanner.nextLine();
	        statement.setInt(1, station1);
	        System.out.println("Enter station 2 id");
	        int station2 = scanner.nextInt();
	        scanner.nextLine();
	        statement.setInt(2, station2);

	        ResultSet rs = statement.executeQuery();

	        printResults(rs);*/
	        return;
	    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findSingleRouteTrips(int selection){
    	try {
			PreparedStatement statement;
			statement = connection.prepareStatement(p.getRoute);
			System.out.println("Enter station 1 id");
			int station1 = scanner.nextInt();
			scanner.nextLine();
			statement.setInt(1, station1);
			System.out.println("Enter station 2 id");
			int station2 = scanner.nextInt();
			scanner.nextLine();
			statement.setInt(2, station2);
			System.out.println("Enter weekday");
			String weekday = scanner.nextLine();
			statement.setString(3, weekday);
			ResultSet rs = statement.executeQuery();

			ArrayList<Integer> routes = new ArrayList<Integer>();

			while(rs.next()){
	            routes.add(rs.getInt("route_id"));
	        }
			HashMap<Integer, Double> values = new HashMap<>();


			switch (selection) {
				case 0: //single route
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						Double distance = 0.0;
						while(rs.next()){
							distance = new Double(rs.getInt("distance"));
						}
						if(distance != 0.0){
							statement = connection.prepareStatement(p.maxSpeed);
							statement.setInt(1, route);
							statement.setString(2, weekday);
							rs = statement.executeQuery();
							while(rs.next()){
								values.put(route, distance / new Double(rs.getInt("SPEED")));
							}
						}
					}
					printSingleTripResults(values.keySet());
					break;
				case 1: //FEWEST STOPS
					//statement = connection.prepareStatement(p.fewestStops);
					for(int route: routes) {
						statement = connection.prepareStatement(p.numberOfStops);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						while(rs.next()){
							int temp = rs.getInt("stops");
							if(temp != 0){
								values.put(route, new Double(temp));
							}
						}
					}
					//SORT THIS BY VALUE ASCENDING

					printTripResults(sortHashMap(values, false), "stops");

					break;
				case 2: //RUN THROUGH MOST STATIONS
					for(int route: routes) {
						statement = connection.prepareStatement(p.numberOfStations);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						while(rs.next()){
							int temp = rs.getInt("stations");
							if(temp != 0){
								values.put(route, new Double(temp));	
							}
						}
					}
					//SORT THIS BY VALUE DESCENDING
					printTripResults(sortHashMap(values, true), "stations");

					//statement = connection.prepareStatement(p.mostStations);
					break;
				case 3: //LOWEST PRICE
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						Double distance = 0.0;
						while(rs.next()){
							distance = new Double(rs.getInt("distance"));
						}
						if(distance != 0.0){
							statement = connection.prepareStatement(p.pricePerMile);
							statement.setInt(1, route);
							statement.setString(2, weekday);
							rs = statement.executeQuery();
							while(rs.next()){
								values.put(route, distance * rs.getDouble("pricepermile"));
							}
						}
					}
					//SORT THIS BY VALUE ASCENDING
					printTripResults(sortHashMap(values, false), "price");


					//statement = connection.prepareStatement(p.lowestPrice);
					break;
				case 4: //HIGHEST PRICE
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						Double distance = 0.0;
						while(rs.next()){
							distance = new Double(rs.getInt("distance"));
						}
						if(distance != 0.0){
							statement = connection.prepareStatement(p.pricePerMile);
							statement.setInt(1, route);
							statement.setString(2, weekday);
							rs = statement.executeQuery();
							while(rs.next()){
								values.put(route, distance * rs.getDouble("pricepermile"));
							}
						}
					}
					//SORT THIS BY VALUE DESCENDING
					printTripResults(sortHashMap(values, true), "price");

					//statement = connection.prepareStatement(p.highestPrice);
					break;
				case 5: //LEAST TOTAL TIME
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						Double distance = 0.0;
						while(rs.next()){
							distance = new Double(rs.getInt("distance"));
						}
						if(distance != 0.0){
							statement = connection.prepareStatement(p.maxSpeed);
							statement.setInt(1, route);
							statement.setString(2, weekday);
							rs = statement.executeQuery();
							while(rs.next()){
								values.put(route, distance / new Double(rs.getInt("SPEED")));
							}
						}
					}
					//SORT THIS BY VALUE ASCENDING
					printTripResults(sortHashMap(values, false), "time");


					//statement = connection.prepareStatement(p.leastTime);
					break;
				case 6: //MOST TOTAL TIME
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						Double distance = 0.0;
						while(rs.next()){
							distance = new Double(rs.getInt("distance"));
						}
						if(distance != 0.0){
							statement = connection.prepareStatement(p.maxSpeed);
							statement.setInt(1, route);
							statement.setString(2, weekday);
							rs = statement.executeQuery();
							while(rs.next()){
								values.put(route, distance / new Double(rs.getInt("SPEED")));
							}
						}
					}
					//SORT THIS BY VALUE DESCENDING
					printTripResults(sortHashMap(values, true), "time");

					//statement = connection.prepareStatement(p.mostTime);
					break;
				case 7: //LEAST TOTAL DISTANCE
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						while(rs.next()){
							int temp = rs.getInt("distance");
							if(temp != 0){
								values.put(route, new Double(temp));
							}
						}
					}
					//SORT THIS BY VALUE ASCENDING
					printTripResults(sortHashMap(values, false), "distance");

					//statement = connection.prepareStatement(p.leastDistance);
					break;
				case 8: //MOST TOTAL DISTANCE
					for(int route: routes) {
						statement = connection.prepareStatement(p.distance);
						statement.setInt(1, route);
						statement.setInt(2, route);
						statement.setInt(3, route);
						statement.setInt(4, route);
						statement.setInt(5, station1);
						statement.setInt(6, route);
						statement.setInt(7, route);
						statement.setInt(8, station2);
						rs = statement.executeQuery();
						while(rs.next()){
							int temp = rs.getInt("distance");
							if(temp != 0){
								values.put(route, new Double(temp));
							}
						}
					}
					//SORT THIS BY VALUE DESCENDING
					printTripResults(sortHashMap(values, true), "distance");
					//statement = connection.prepareStatement(p.mostDistance);
					break;
				case 9:    //BACK TO findTripsSubMenu
					System.out.println("1.) Single Route Trip Search\n2.) Combination Route Trip Search\n" +
							"3.) Add Reservation\n4.) More\n5.) back");
					int choice = scanner.nextInt();
					scanner.nextLine();
					findTripSubMenu(choice);
					return;
				default: //INVALID INPUT
					System.out.println("Pick a valid number\n");
					System.out.println("0.) No sort\n1.) Fewest stops\n2.) Run through most stations\n" +
							"3.) Lowest price\n4.) Highest price\n5.) Least total time\n6.) Most total time\n" +
							"7.) Least total distance\n8.) Most total distance\n 9.) back");
					int thirdChoice = scanner.nextInt();
					findSingleRouteTrips(thirdChoice);
					return;
			}
			
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }



	public static void findCombinationTrips(int selection){
    	try {

    		System.out.println("Enter station 1 id");
	    	int station1 = scanner.nextInt();
	    	scanner.nextLine();
	    	System.out.println("Enter station 2 id");
			int station2 = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter weekday");
			String weekday = scanner.nextLine();
	    	PreparedStatement statement = connection.prepareStatement(p.combinationStop1);
	    	statement.setInt(1, station1);
	    	statement.setString(2, weekday);
			statement.setInt(3, station2);
			statement.setString(4, weekday);
			ResultSet rs = statement.executeQuery();

			ArrayList<Integer> routesA = new ArrayList<Integer>();

			while(rs.next()){
	            routesA.add(rs.getInt("route_id"));
	        }

	        //System.out.println(routesA);
	        Map<Integer, Time> departure = new HashMap<>();

	        Map<Integer, List<Integer>> stopsAfterA = new HashMap<>();
	        for(int route: routesA) {
	            statement = connection.prepareStatement(p.combinationStop2);
	            statement.setInt(1, route);
	            statement.setInt(2, route);
	            statement.setInt(3, route);
	            statement.setInt(4, route);
	            statement.setInt(5, station1);
	            rs = statement.executeQuery();
	            List<Integer> temp = new ArrayList<>();
	            while(rs.next()){
	            	temp.add(rs.getInt("station_b_id"));
	            }
	            stopsAfterA.put(route, temp);
	            statement = connection.prepareStatement("SELECT runtime FROM schedule WHERE route_id = ? AND weekday = ?;");
	            statement.setInt(1, route);
	            statement.setString(2, weekday);
	            rs = statement.executeQuery();
	            while(rs.next()){
	            	departure.put(route, rs.getTime("runtime"));
	            }
	        }
	        //System.out.println(stopsAfterA);
	        Set<Integer> stopsA = new HashSet<Integer>();
	        for(List<Integer> list: stopsAfterA.values()){
	        	for(Integer val: list){
	        		stopsA.add(val);
	        	}
	        }
	        //System.out.println(stopsA);

	        statement = connection.prepareStatement(p.combinationStop3);
	        statement.setInt(1, station2);
	        statement.setString(2, weekday);
	        statement.setInt(3, station1);
	        statement.setString(4, weekday);
	        rs = statement.executeQuery();

	        ArrayList<Integer> routesB = new ArrayList<Integer>();

	        while(rs.next()){
	        	routesB.add(rs.getInt("route_id"));
	        }

	        //System.out.println(routesB);

	        Map<Integer, List<Integer>> stopsBeforeB = new HashMap<>();
	        for(int route: routesB){
	        	statement = connection.prepareStatement(p.combinationStop4);
	        	statement.setInt(1, route);
	        	statement.setInt(2, route);
	        	statement.setInt(3, route);
	        	statement.setInt(4, route);
	        	statement.setInt(5, station2);
	        	rs = statement.executeQuery();
	       		List<Integer> temp = new ArrayList<>();
	        	while(rs.next()){
	        		temp.add(rs.getInt("station_a_id"));
	        	}
	        	stopsBeforeB.put(route, temp);
	        	statement = connection.prepareStatement("SELECT runtime FROM schedule WHERE route_id = ? AND weekday = ?;");
	            statement.setInt(1, route);
	            statement.setString(2, weekday);
	            rs = statement.executeQuery();
	            while(rs.next()){
	            	departure.put(route, rs.getTime("runtime"));
	            }
	        }
	        //System.out.println(stopsBeforeB);
	        Set<Integer> stopsB = new HashSet<Integer>();
	        for(List<Integer> list: stopsBeforeB.values()){
	        	for(Integer val: list){
	        		stopsB.add(val);
	        	}
	        }
	        //System.out.println(stopsB);

	        //System.out.println("THINGS ONLY IN BOTH");
	        //stopsA.retainAll(stopsB);
	        //System.out.println(stopsA);
	        //System.out.println(stops);
	        ArrayList<CombinationLeg> combo = new ArrayList<CombinationLeg>();
	        for(Integer stop: stopsA){
		        for(Integer routeA: routesA){
		        	if(stopsAfterA.get(routeA).contains(stop)){
		        		for(Integer routeB: routesB){
				        	if(stopsBeforeB.get(routeB).contains(stop) && departure.get(routeA).toString().compareTo(departure.get(routeB).toString()) < 0){
				        		combo.add(new CombinationLeg(station1, station2, routeA, stop, routeB));
				        	}
				        }
		        	}
		        }
		   	}
		   	new CombinationLeg(1, 2, 3, 4, 5).clearUtil();
		   	/**
		   	for(CombinationLeg leg: combo){
		   		System.out.println(leg);
		   	}
		   	System.out.println(combo.size());
		   	**/

		   	HashMap<String, Double> values = new HashMap<>();

	        switch (selection){
	        	case 0: //no sort
					printNoSortCombinationResults(combo);
	        		break;
	        	case 1: //FEWEST STOPS
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getStops());
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, false), "stops");
	        		break;
	        	case 2: //RUN THROUGH MOST STATIONS
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getStations());
	        		}
	        		printCombinedTripResults(sortCombinedHashMap(values, true), "stations");
	        		break;
	        	case 3: //LOWEST PRICE
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getPrice(weekday));
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, false), "price");
	        		break;
	        	case 4: //HIGHEST PRICE
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getPrice(weekday));
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, true), "price");
	        		break;
	        	case 5: //LEAST TOTAL TIME
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getTime(weekday));
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, false), "time");
					break;
	        	case 6: //MOST TOTAL TIME
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getTime(weekday));
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, true), "time");
					break;
	        	case 7: //LEAST TOTAL DISTANCE
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getDistance());
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, false), "distance");
	        		break;
	        	case 8: //MOST TOTAL DISTANCE
	        		for(CombinationLeg leg: combo){
	        			values.put(leg.toString(), leg.getDistance());
	        		}
					printCombinedTripResults(sortCombinedHashMap(values, true), "distance");
					break;
	        	case 9: //BACK TO
	        		break;
	        	default:
	        		System.out.println("Pick a valid number\n");
					System.out.println("0.) No sort\n1.) Fewest stops\n2.) Run through most stations\n" +
							"3.) Lowest price\n4.) Highest price\n5.) Least total time\n6.) Most total time\n" +
							"7.) Least total distance\n8.) Most total distance\n 9.) back");
					int thirdChoice = scanner.nextInt();
					findCombinationTrips(thirdChoice);
					return;
	        }
	    } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static LinkedHashMap<Integer, Double> sortHashMap(HashMap<Integer, Double> values, Boolean reverse){
    	List<Integer> mapKeys = new ArrayList<>(values.keySet());
	    List<Double> mapValues = new ArrayList<>(values.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    if(reverse) {
			Collections.reverse(mapValues);
			Collections.reverse(mapKeys);
	    }

		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Double val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Integer key = keyIt.next();
	            Double comp2 = values.get(key);
	            Double comp1 = val;
		       
	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
    }

	public static LinkedHashMap<String, Double> sortCombinedHashMap(HashMap<String, Double> values, Boolean reverse){
		List<String> mapKeys = new ArrayList<>(values.keySet());
		List<Double> mapValues = new ArrayList<>(values.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		if(reverse) {
			Collections.reverse(mapValues);
			Collections.reverse(mapKeys);
		}

		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

		Iterator<Double> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Double val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				String key = keyIt.next();
				Double comp2 = values.get(key);
				Double comp1 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}

    public static void advancedSearchesSubMenu(int selection){
    	// specificStationDayTime -     station_id, weekday, time
        // moreThanOneRail -            no params
        // sameStationDifferentStop -   route_ID
        // allTrainsPass -              no params
        // doNotStopAtStation -         station_id
        // percentStops -               0 >= percentage <= 1
        // routeSchedule -              route_id
        // availableDayTime -           route_id, weekday, time
        try{
	        PreparedStatement statement;
	        int station, route;
	        String weekday, time;
	        Time t;
	        double per;

	    	switch (selection){
	    		case 1://FIND ALL TRAINS THAT PASS THROUGH A SPECIFIC STATION AT A SPECIFIC DAT/TIME COMBO
	    			statement = connection.prepareStatement(p.specificStationDayTime);
	                System.out.println("Enter station id");
	                station = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(1, station);
					statement.setInt(2, station);

					System.out.println("Enter weekday");
	                weekday = scanner.nextLine();
	                statement.setString(3, weekday);
	                System.out.println("Enter time (xx:xx:xx)");
	                time = scanner.next();
	                t = Time.valueOf(time);
	                statement.setTime(4, t);
	    			break;
	    		case 2: //FIND ALL THE ROUTES THAT TRAVEL MORE THAN ONE RAIL LINE
	    			statement = connection.prepareStatement(p.moreThanOneRail);
	    			break;
	    		case 3: //FIND ROTES THAT PASS THROUGH THE SAME STATIONS BUT DON'T HAVE THE SAME STOPS
	    			statement = connection.prepareStatement(p.sameStationDifferentStop);
	                System.out.println("Enter route id");
	                route = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(1, route);
					statement.setInt(2, route);

					statement.setInt(3, route);

					break;
	    		case 4: //FIND ANY STATIONS THROUGH WHICH ALL TRANS PASS THROUGH
	    			statement = connection.prepareStatement(p.allTrainsPass);
	    			break;
	    		case 5: //FIND ALL THE TRAINS THAT DO NOT STOP AT A SPECIFIC STATION
	    			statement = connection.prepareStatement(p.doNotStopAtStation);
	                System.out.println("Enter station id");
	                station = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(1, station);
	    			break;
	    		case 6: //FIND ROUTES THAT STOP AT LEAST AT XX% OF THE STATIONS THEY VISIT
	    			statement = connection.prepareStatement(p.percentStops);
	                System.out.println("Enter a percentage (0 - 1)");
	                per = scanner.nextDouble();
	                scanner.nextLine();
	                statement.setDouble(1, per);
	    			break;
	    		case 7: //DISPLAY THE SCHEDULE OF A ROUTE
	    			statement = connection.prepareStatement(p.routeSchedule);
	                System.out.println("Enter route id");
	                route = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(1, route);
	    			break;
	    		case 8: //AVAILABILITY OF A ROUTE AT EVERY STOP ON A SPECIFIC DAY AND TIME
	    			statement = connection.prepareStatement(p.availableDayTime);
	                System.out.println("Enter route id");
	                route = scanner.nextInt();
	                scanner.nextLine();
	                statement.setInt(1, route);
	                System.out.println("Enter weekday");
	                weekday = scanner.nextLine();
	                statement.setString(2, weekday);
	                System.out.println("Enter time (xx:xx:xx)");
	                time = scanner.next();
	                t = Time.valueOf(time);
	                statement.setTime(3, t);
	    			break;
	    		case 9: //BACK
	    			return;
	    		default: //INVALID INPUT
	    			System.out.println("Pick a valid number\n");
	    			System.out.println("1.) Find all trains that pass through a specific station at a specific\n" +
	                            "day/time combination\n2.) Find the routes that travel more than one rail line\n" +
	                            "3.) Find routes that pass through the same stations but don’t have\n" +
	                            "the same stops\n4.) Find any stations through which all trains pass through" +
	                            "\n5.) Find all the trains that do not stop at a specific station\n6.) Find routes that stop at least at XX% of the Stations they visit" +
	                            "\n7.) Display the schedule of a route\n" +
	                            "8.) Find the availability of a route at every stop on a specific day and time\n9.) back");
	    			int choice = scanner.nextInt();
	                scanner.nextLine();
	                advancedSearchesSubMenu(choice);
	    			return;
	    	}
	    	ResultSet rs = statement.executeQuery();
	        printResults(rs);
	    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dbaSubMenu(int selection){
    	try{
	    	Statement statement;
	    	switch (selection){
	    		case 1: //RESTORE FROM BACKUP
	    			//CHECK TO MAKE SURE FILES EXIST
	    			if(!new File("Database").exists()){
	                    System.out.println("Database directory missing");
	                    return;
	                }
	                for(int i = 0; i < tables.length; i++){
	                    if(!new File("Database/" + tables[i] + ".csv").exists()){
	                        System.out.println("Database/" + tables[i] + ".csv missing");
	                        return;
	                    }
	                }
	                //DELETE DB
	                deleteDB();
	                //RESTORE FROM BACKUP
	                for(int i = 0; i < tables.length; i++){
	                    try{
	                        List<String> create = Files.readAllLines(Paths.get("Database/" + tables[i] + ".csv"), StandardCharsets.UTF_8);
	                        System.out.println(create);
	                        statement = connection.createStatement();
	                        statement.executeUpdate("INSERT INTO " + tables[i] + " VALUES " + create.get(0));
	                    }catch(IOException e){
	                        e.printStackTrace();
	                    }
	                
	                }
	                break;
	    		case 2: //BACKUP DB
	    			//MAKE DIRECTORY IF IT DOESN'T EXIST
	    			if(!new File("Database").exists()){
	                    new File("Database").mkdirs();
	                }
	                for(int i = 0; i < tables.length; i ++){
	                    System.out.println("Exporting " + tables[i]);
	                    ArrayList<String> dump = new ArrayList<>();
	                    try{
	                        statement = connection.createStatement();
	                        ResultSet rs = statement.executeQuery("SELECT * FROM " + tables[i]);
	                        int numCols = rs.getMetaData().getColumnCount();
	                        while(rs.next()){
	                            StringBuilder sb = new StringBuilder("(");
	                            for(int j = 1; j <= numCols; j++){
	                                String type = rs.getMetaData().getColumnTypeName(j);
	                                if(type.equals("time") || type.equals("varchar")){
	                                    sb.append("'" + String.format(String.valueOf(rs.getString(j))) + "',");
	                                }else if(type.equals("bool")){ 
	                                    if(String.valueOf(rs.getString(j)).equals("t")){
	                                        sb.append("true,");
	                                    }else{
	                                        sb.append("false,");
	                                    }
	                                }else{
	                                    sb.append(String.format(String.valueOf(rs.getString(j))) + ",");
	                                }
	                            }
	                            sb.setLength(sb.length() - 1);
	                            sb.append("),");
	                            dump.add(sb.toString());
	                        }
	                        String last = dump.remove(dump.size() - 1);
	                        dump.add(last.substring(0, last.length() - 1));
	                        FileWriter writer = new FileWriter(new File("Database/" + tables[i] + ".csv"));
	                        for(String entry : dump){
	                            writer.write(entry);
	                        }
	                        writer.close();
	                    }catch(IOException e){
	                        e.printStackTrace();
	                    }
	                }
	                System.out.println("All tables exported to ./Database/");
	                break;
	    		case 3: //DELETE DATA
	    			deleteDB();
	    			break;
	    		case 4: //BACK
	    			return;
	    		default: //INVALID INPUT
	    			System.out.println("Pick a valid number\n");
	    			System.out.println("1.) Import Database\n2.) Export Database\n" +
	                            "3.) Delete Database\n4.) back");
	            	int choice = scanner.nextInt();
	            	scanner.nextLine();
	        		dbaSubMenu(choice);
	        		return;
	    	}
	    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDB(){
    	try{
	    	Statement stmt;
	    	for(int i = tables.length - 1 ; i >= 0; i--){
	            System.out.println("Deleting " + tables[i]);
	            stmt = connection.createStatement();

	            stmt.executeUpdate("DELETE FROM " + tables[i]);
	        }
	    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printResults(ResultSet rs) {
        try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			//better to handle pagination in java bc you dont have to re run query
			int count = 0;
			if (!rs.next())
				System.out.println("No results - either route doesn't exist or train is full. check availability");

			else {
				do {
					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) System.out.print(",  ");
						String columnValue = rs.getString(i);
						System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
					}
					System.out.println();
					count++;
					if(count % 10 == 0) {
						System.out.println("1.) show more\n2.) back");
						int c = scanner.nextInt();
						scanner.nextLine();
						if(c == 2)
							break;
					}
				} while (rs.next());
			}
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

	private static void printTripResults(LinkedHashMap<Integer, Double> sortedHashMap, String metric) {
		Iterator it = sortedHashMap.entrySet().iterator();
		int count = 0;

		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			System.out.println("Route_ID: " + e.getKey() + ", " + metric + ": " + e.getValue());
			count++;
			if(count % 10 == 0) {
				System.out.println("1.) show more\n2.) back");
				int c = scanner.nextInt();
				scanner.nextLine();
				if(c == 2)
					break;
			}
		}
	}

	private static void printCombinedTripResults(LinkedHashMap<String, Double> sortedHashMap, String metric) {
		Iterator it = sortedHashMap.entrySet().iterator();
		int count = 0;

		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			System.out.println("Route Combination: " + e.getKey() + ", " + metric + ": " + e.getValue());
			count++;
			if(count % 10 == 0) {
				System.out.println("1.) show more\n2.) back");
				int c = scanner.nextInt();
				scanner.nextLine();
				if(c == 2)
					break;
			}
		}
	}

	private static void printSingleTripResults(Set<Integer> keySet) {
		Iterator it = keySet.iterator();
		int count = 0;

		while (it.hasNext()) {
			System.out.println("Route_ID: " + it.next());
			count++;
			if(count % 10 == 0) {
				System.out.println("1.) show more\n2.) back");
				int c = scanner.nextInt();
				scanner.nextLine();
				if(c == 2)
					break;
			}
		}
	}

	private static void printNoSortCombinationResults(ArrayList<CombinationLeg> keySet) {
		Iterator it = keySet.iterator();
		int count = 0;

		while (it.hasNext()) {
			System.out.println(it.next().toString());
			count++;
			if(count % 10 == 0) {
				System.out.println("1.) show more\n2.) back");
				int c = scanner.nextInt();
				scanner.nextLine();
				if(c == 2)
					break;
			}
		}
	}

}
