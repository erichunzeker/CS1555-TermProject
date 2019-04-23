import java.util.*;
import java.sql.*;

public class CombinationLeg {

    public int origin, destination, routeFromA, intermediatePoint, routeToB;
    public static Map<String, Double> util = new HashMap<>();
    static ParameterizedQueries p;
    public ResultSet rs;
   	static Connection connection;
   	static RailWay parent;

    public CombinationLeg(int start, int finish, int routeA, int midPoint, int routeB) {
        origin = start;
        destination = finish;
        routeFromA = routeA;
        intermediatePoint = midPoint;
        routeToB = routeB;
        connection = parent.connection;

    }

    public String toString(){//overriding the toString() method  
  		return origin + " Route: " + routeFromA + " -> " + intermediatePoint + " Route: " + routeToB + " -> " + destination;
 	}  

 	public double getDistance(){
 		try {
	 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
	 			PreparedStatement statement = connection.prepareStatement(p.distance);
	 			statement.setInt(1, routeFromA);
				statement.setInt(2, routeFromA);
				statement.setInt(3, routeFromA);
				statement.setInt(4, routeFromA);
				statement.setInt(5, origin);
				statement.setInt(6, routeFromA);
				statement.setInt(7, routeFromA);
				statement.setInt(8, intermediatePoint);
				rs = statement.executeQuery();
				while(rs.next()){
					util.put(origin + "-" + routeFromA + "-" + intermediatePoint, new Double(rs.getInt("distance")));
				}
	 		}
	 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
	 			PreparedStatement statement = connection.prepareStatement(p.distance);
	 			statement.setInt(1, routeToB);
				statement.setInt(2, routeToB);
				statement.setInt(3, routeToB);
				statement.setInt(4, routeToB);
				statement.setInt(5, intermediatePoint);
				statement.setInt(6, routeToB);
				statement.setInt(7, routeToB);
				statement.setInt(8, destination);
				rs = statement.executeQuery();
				while(rs.next()){
					util.put(intermediatePoint + "-" + routeToB + "-" + destination, new Double(rs.getInt("distance")));
				}
	 		}
	 		return util.get(origin + "-" + routeFromA + "-" + intermediatePoint) + util.get(origin + "-" + routeFromA + "-" + intermediatePoint);
	 	} catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
 	}

 	public double getStops(){
 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
 			//CALCULATE AND STORE
 		}
 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
 			//CALCULATE AND STORE
 		}
 		//RETURN both combined

 		return 0;
 	}

 	public double getStations(){
 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
 			//CALCULATE AND STORE
 		}
 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
 			//CALCULATE AND STORE
 		}
 		//RETURN both combined
 		return 0;
 	}

 	public double getTime(){
 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
 			//CALCULATE AND STORE
 		}
 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
 			//CALCULATE AND STORE
 		}
 		//RETURN both combined
 		return 0;
 	}

 	public double getPrice(){
 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
 			//CALCULATE AND STORE
 		}
 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
 			//CALCULATE AND STORE
 		}
 		//RETURN both combined
 		return 0;
 	}
}
