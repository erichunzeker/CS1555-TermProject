import java.util.*;
import java.sql.*;

public class CombinationLeg {

    public int origin, destination, routeFromA, intermediatePoint, routeToB;
    public static Map<String, Double> util = new HashMap<>();
    public ParameterizedQueries p;
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
        p = new ParameterizedQueries();

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
 		try {
	 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
	 			PreparedStatement statement = connection.prepareStatement(p.numberOfStops);
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
					util.put(origin + "-" + routeFromA + "-" + intermediatePoint, new Double(rs.getInt("stops")));
				}
	 		}
	 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
	 			PreparedStatement statement = connection.prepareStatement(p.numberOfStops);
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
					util.put(intermediatePoint + "-" + routeToB + "-" + destination, new Double(rs.getInt("stops")));
				}
	 		}
	 		return util.get(origin + "-" + routeFromA + "-" + intermediatePoint) + util.get(origin + "-" + routeFromA + "-" + intermediatePoint);
	 	} catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
 	}

 	public double getStations(){
 		try {
	 		if(!util.containsKey(origin + "-" + routeFromA + "-" + intermediatePoint)) {
	 			PreparedStatement statement = connection.prepareStatement(p.numberOfStations);
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
					util.put(origin + "-" + routeFromA + "-" + intermediatePoint, new Double(rs.getInt("stations")));
				}
	 		}
	 		if(!util.containsKey(intermediatePoint + "-" + routeToB + "-" + destination)) {
	 			PreparedStatement statement = connection.prepareStatement(p.numberOfStations);
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
					util.put(intermediatePoint + "-" + routeToB + "-" + destination, new Double(rs.getInt("stations")));
				}
	 		}
	 		return util.get(origin + "-" + routeFromA + "-" + intermediatePoint) + util.get(origin + "-" + routeFromA + "-" + intermediatePoint);
	 	} catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
 	}

 	public double getTime(String weekday){
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
				Double distance = 0.0;
				while(rs.next()){
					distance = new Double(rs.getInt("distance"));
				}
				statement = connection.prepareStatement(p.maxSpeed);
				statement.setInt(1, routeFromA);
				statement.setString(2, weekday);
				rs = statement.executeQuery();
				while(rs.next()){
					util.put(origin + "-" + routeFromA + "-" + intermediatePoint, distance / new Double(rs.getInt("SPEED")));
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
				Double distance = 0.0;
				while(rs.next()){
					distance = new Double(rs.getInt("distance"));
				}
				statement = connection.prepareStatement(p.maxSpeed);
				statement.setInt(1, routeToB);
				statement.setString(2, weekday);
				rs = statement.executeQuery();
				while(rs.next()){
					util.put(intermediatePoint + "-" + routeToB + "-" + destination, distance / new Double(rs.getInt("SPEED")));
				}
	 		}
	 		return util.get(origin + "-" + routeFromA + "-" + intermediatePoint) + util.get(origin + "-" + routeFromA + "-" + intermediatePoint);
	 	} catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
 	}

 	public double getPrice(String weekday){
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
				Double distance = 0.0;
				while(rs.next()){
					distance = new Double(rs.getInt("distance"));
				}
				statement = connection.prepareStatement(p.pricePerMile);
				statement.setInt(1, routeFromA);
				statement.setString(2, weekday);
				rs = statement.executeQuery();
				while(rs.next()){
					util.put(origin + "-" + routeFromA + "-" + intermediatePoint, distance * rs.getDouble("pricepermile"));
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
				Double distance = 0.0;
				while(rs.next()){
					distance = new Double(rs.getInt("distance"));
				}
				statement = connection.prepareStatement(p.pricePerMile);
				statement.setInt(1, routeToB);
				statement.setString(2, weekday);
				rs = statement.executeQuery();
				while(rs.next()){
					util.put(intermediatePoint + "-" + routeToB + "-" + destination, distance * rs.getDouble("pricepermile"));
				}
	 		}
	 		return util.get(origin + "-" + routeFromA + "-" + intermediatePoint) + util.get(origin + "-" + routeFromA + "-" + intermediatePoint);
	 	} catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
 	}
}
