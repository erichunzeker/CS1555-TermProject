SELECT R.route_id
    FROM schedule S
    INNER JOIN train T
    ON S.Train_ID = T.train_id
    INNER JOIN route_stop RS
    ON S.Route_ID = RS.Route_ID
    INNER JOIN stop
    ON RS.Stop_ID = stop.Stop_ID
    INNER JOIN route R
    ON RS.Route_ID = R.route_id
    WHERE
      Station_B_ID = ? AND Stops_At_B = TRUE AND weekday = ? AND R.route_id not in 
      	(SELECT R.route_id
		  FROM schedule S
		  INNER JOIN train T
		  ON S.Train_ID = T.train_id
		  INNER JOIN route_stop RS
		  ON S.Route_ID = RS.Route_ID
		  INNER JOIN stop
		  ON RS.Stop_ID = stop.Stop_ID
		  INNER JOIN route R
		  ON RS.Route_ID = R.route_id
		  WHERE
		      Station_A_ID = ? AND Stops_At_A = TRUE AND weekday = ?);

?
1=destination
2=4=Weekday
3=origin

SELECT R.route_id
    FROM schedule S
    INNER JOIN train T
    ON S.Train_ID = T.train_id
    INNER JOIN route_stop RS
    ON S.Route_ID = RS.Route_ID
    INNER JOIN stop
    ON RS.Stop_ID = stop.Stop_ID
    INNER JOIN route R
    ON RS.Route_ID = R.route_id
    WHERE
      Station_B_ID = 30 AND Stops_At_B = TRUE AND weekday = 'Sun';

?
1=origin stop
2=destination stop
3=Weekday

--FOR EACH OF THE ROUTE ID RETURNED ABOVE DO THIS
--GET ALL STOPS AFTER ORIGIN
SELECT * FROM(
	WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id, distancebetween) AS (
    	SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id, distancebetween
   		FROM route_stop, stop
    	WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = 11) AND route_id = 11
  	UNION ALL
    	SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id, s.distancebetween
    	FROM sortroute sr, route_stop rs, stop s
    	WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)
	)
	SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A
WHERE row > (
	SELECT row FROM(
		WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (
    		SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id
   			FROM route_stop, stop
    		WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = 11) AND route_id = 11
  		UNION ALL
	    	SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id
	    	FROM sortroute sr, route_stop rs, stop s
	    	WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)
		)
		SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A
	WHERE station_A_id = 11) AND stops_at_b = TRUE;

?
1=2=3=4=route id from previous
5=origin stop


SELECT * FROM(
	WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id, distancebetween) AS (
    	SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id, distancebetween
   		FROM route_stop, stop
    	WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = 211) AND route_id = 211
  	UNION ALL
    	SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id, s.distancebetween
    	FROM sortroute sr, route_stop rs, stop s
    	WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)
	)
	SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A


-- DISTANCE TRAVELED
SELECT SUM(distancebetween) FROM(
	WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id, distancebetween) AS (
    	SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id, distancebetween
   		FROM route_stop, stop
    	WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?
  	UNION ALL
    	SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id, s.distancebetween
    	FROM sortroute sr, route_stop rs, stop s
    	WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)
	)
	SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A
WHERE row > (
	SELECT row FROM(
		WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (
    		SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id
   			FROM route_stop, stop
    		WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?
  		UNION ALL
	    	SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id
	    	FROM sortroute sr, route_stop rs, stop s
	    	WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)
		)
		SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A
	WHERE station_A_id = ?) AND row <= (
	SELECT row FROM(
		WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (
    		SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id
   			FROM route_stop, stop
    		WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?
  		UNION ALL
	    	SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id
	    	FROM sortroute sr, route_stop rs, stop s
	    	WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)
		)
		SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A
	WHERE station_B_id = ?);

?
1=2=3=4=6=7=route
5=origin
8=destination