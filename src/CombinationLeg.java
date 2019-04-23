public class CombinationLeg {

    public int origin, destination, routeFromA, intermediatePoint, routeToB;

    public CombinationLeg(int start, int finish, int routeA, int midPoint, int routeB) {
        origin = start;
        destination = finish;
        routeFromA = routeA;
        intermediatePoint = midPoint;
        routeToB = routeB;
    }

    public String toString(){//overriding the toString() method  
  		return origin + " Route: " + routeFromA + " -> " + intermediatePoint + " Route: " + routeToB + " -> " + destination;
 	}  
}
