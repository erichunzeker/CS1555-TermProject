public class ParameterizedQueries {

    public String addCustomer, editCustomer, viewCustomer;
    public String singleRoute, fewestStops, mostStations, lowestPrice, highestPrice, leastTime, mostTime, leastDistance, mostDistance, addReservation;
    public String combinationStop1, combinationStop2, combinationStop3, combinationStop4;
    public String getRoute, distance, numberOfStops, numberOfStations, pricePerMile, maxSpeed;
    public String specificStationDayTime, moreThanOneRail, sameStationDifferentStop, allTrainsPass, doNotStopAtStation, percentStops, routeSchedule, availableDayTime;

    public ParameterizedQueries() {
        //Update customer list
        addCustomer = "INSERT INTO passenger (firstname, lastname, phone, street, city, state, zip, country, email)\n" +
                "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING passenger_id;";

        editCustomer = "UPDATE passenger SET\n" +
                "  (firstname, lastname, phone, street, city, state, zip, country, email) = (?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "  WHERE passenger_id =?\n" +
                "  RETURNING passenger_id;";

        viewCustomer = "SELECT *\n" +
                "  FROM passenger\n" +
                "  WHERE passenger_id = ?;";

        //Finding a trip between two stations

        singleRoute = "SELECT *\n" +
                "  FROM (SELECT R.route_id, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "      INTERSECT\n" +
                "    SELECT R.route_id, weekday, seats_taken, seats\n" +
                "      FROM schedule S\n" +
                "      INNER JOIN train T\n" +
                "      ON S.Train_ID = T.train_id\n" +
                "      INNER JOIN route_stop RS\n" +
                "      ON S.Route_ID = RS.Route_ID\n" +
                "      INNER JOIN stop\n" +
                "      ON RS.Stop_ID = stop.Stop_ID\n" +
                "      INNER JOIN route R\n" +
                "      ON RS.Route_ID = R.route_id\n" +
                "      WHERE\n" +
                "        Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "    ) as A\n " +
                "    WHERE A.weekday = ? AND seats_taken < seats;";

        // begin aggregate functions
        // might have to duplicate all aggregate functions for 1.2.2

        getRoute ="SELECT route_id\n" +
                "  FROM (SELECT R.route_id, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "      INTERSECT\n" +
                "    SELECT R.route_id, weekday, seats_taken, seats\n" +
                "      FROM schedule S\n" +
                "      INNER JOIN train T\n" +
                "      ON S.Train_ID = T.train_id\n" +
                "      INNER JOIN route_stop RS\n" +
                "      ON S.Route_ID = RS.Route_ID\n" +
                "      INNER JOIN stop\n" +
                "      ON RS.Stop_ID = stop.Stop_ID\n" +
                "      INNER JOIN route R\n" +
                "      ON RS.Route_ID = R.route_id\n" +
                "      WHERE\n" +
                "        Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "    ) as A\n " +
                "    WHERE A.weekday = ? AND seats_taken < seats;";

        distance = "SELECT SUM(distancebetween) AS distance FROM(\n" +
                "   WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id, distancebetween) AS (\n" +
                "       SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id, distancebetween\n" +
                "           FROM route_stop, stop\n" +
                "       WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "   UNION ALL\n" +
                "       SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id, s.distancebetween\n" +
                "       FROM sortroute sr, route_stop rs, stop s\n" +
                "       WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "   )\n" +
                "   SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "WHERE row >= (\n" +
                "   SELECT row FROM(\n" +
                "       WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "           SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "               FROM route_stop, stop\n" +
                "           WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "       UNION ALL\n" +
                "           SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "           FROM sortroute sr, route_stop rs, stop s\n" +
                "           WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "       )\n" +
                "       SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "   WHERE station_A_id = ?) AND row <= (\n" +
                "   SELECT row FROM(\n" +
                "       WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "           SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "               FROM route_stop, stop\n" +
                "           WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "       UNION ALL\n" +
                "           SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "           FROM sortroute sr, route_stop rs, stop s\n" +
                "           WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "       )\n" +
                "       SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "   WHERE station_B_id = ?);";

        numberOfStops = "SELECT count(*) AS stops FROM(\n" +
                "   WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id, distancebetween) AS (\n" +
                "       SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id, distancebetween\n" +
                "           FROM route_stop, stop\n" +
                "       WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "   UNION ALL\n" +
                "       SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id, s.distancebetween\n" +
                "       FROM sortroute sr, route_stop rs, stop s\n" +
                "       WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "   )\n" +
                "   SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "WHERE stops_at_b = TRUE and row >= (\n" +
                "   SELECT row FROM(\n" +
                "       WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "           SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "               FROM route_stop, stop\n" +
                "           WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "       UNION ALL\n" +
                "           SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "           FROM sortroute sr, route_stop rs, stop s\n" +
                "           WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "       )\n" +
                "       SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "   WHERE station_A_id = ?) AND row <= (\n" +
                "   SELECT row FROM(\n" +
                "       WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "           SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "               FROM route_stop, stop\n" +
                "           WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "       UNION ALL\n" +
                "           SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "           FROM sortroute sr, route_stop rs, stop s\n" +
                "           WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "       )\n" +
                "       SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "   WHERE station_B_id = ?);";

        numberOfStations ="SELECT count(*) AS stations FROM(\n" +
                "   WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id, distancebetween) AS (\n" +
                "       SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id, distancebetween\n" +
                "           FROM route_stop, stop\n" +
                "       WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "   UNION ALL\n" +
                "       SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id, s.distancebetween\n" +
                "       FROM sortroute sr, route_stop rs, stop s\n" +
                "       WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "   )\n" +
                "   SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "WHERE row >= (\n" +
                "   SELECT row FROM(\n" +
                "       WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "           SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "               FROM route_stop, stop\n" +
                "           WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "       UNION ALL\n" +
                "           SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "           FROM sortroute sr, route_stop rs, stop s\n" +
                "           WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "       )\n" +
                "       SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "   WHERE station_A_id = ?) AND row <= (\n" +
                "   SELECT row FROM(\n" +
                "       WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "           SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "               FROM route_stop, stop\n" +
                "           WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "       UNION ALL\n" +
                "           SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "           FROM sortroute sr, route_stop rs, stop s\n" +
                "           WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "       )\n" +
                "       SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "   WHERE station_B_id = ?);";

        pricePerMile = "SELECT pricepermile FROM schedule S, route R, train T\n" +
                "WHERE S.route_id = R.route_id AND S.train_id = T.train_id AND S.route_id = ? AND S.weekday = ?;";

        maxSpeed = "SELECT LEAST(topspeed, speedlimit) AS SPEED FROM schedule S, route R, train T, railline_route RR, railline ra\n" +
                "WHERE S.route_id = R.route_id AND S.train_id = T.train_id AND R.route_id = RR.route_id AND RR.railline_id = ra.railline_id AND S.route_id = ? AND S.weekday = ?;";

        fewestStops = "SELECT A.route_id, count(Stop_ID) as stop_count, schedule.runtime\n" +
                "  FROM\n" +
                "  (SELECT R.route_id, R.description\n" +
                "  FROM route R\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON R.route_id = RS.Route_ID\n" +
                "  INNER JOIN stop S\n" +
                "  ON RS.Stop_ID = S.Stop_ID\n" +
                "    WHERE Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "    INTERSECT\n" +
                "  SELECT R.route_id, R.description\n" +
                "  FROM route R\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON R.route_id = RS.Route_ID\n" +
                "  INNER JOIN stop S\n" +
                "  ON RS.Stop_ID = S.Stop_ID\n" +
                "    WHERE Station_B_ID = ? AND Stops_At_B = TRUE) AS A\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON RS.Route_ID = A.route_id\n" +
                "  INNER JOIN schedule\n" +
                "  ON schedule.Route_ID = A.route_id\n" +
                "  INNER JOIN train\n" +
                "  ON schedule.Train_ID = train.train_id\n" +
                "  WHERE schedule.weekday = ? AND seats_taken < seats\n" +
                "  GROUP BY A.route_id, schedule.runtime\n" +
                "  ORDER BY stop_count ASC;";

        mostStations = "SELECT A.route_id, count(Stop_ID) as stop_count, schedule.runtime\n" +
                "  FROM\n" +
                "  (SELECT R.route_id, R.description\n" +
                "  FROM route R\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON R.route_id = RS.Route_ID\n" +
                "  INNER JOIN stop S\n" +
                "  ON RS.Stop_ID = S.Stop_ID\n" +
                "    WHERE Station_A_ID = ?\n" +
                "    INTERSECT\n" +
                "  SELECT R.route_id, R.description\n" +
                "  FROM route R\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON R.route_id = RS.Route_ID\n" +
                "  INNER JOIN stop S\n" +
                "  ON RS.Stop_ID = S.Stop_ID\n" +
                "    WHERE Station_B_ID = ?) AS A\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON RS.Route_ID = A.route_id\n" +
                "  INNER JOIN schedule\n" +
                "  ON schedule.Route_ID = A.route_id\n" +
                "  INNER JOIN train\n" +
                "  ON schedule.Train_ID = train.train_id\n" +
                "  WHERE schedule.weekday = ? AND seats_taken < seats\n" +
                "  GROUP BY A.route_id, schedule.runtime\n" +
                "  ORDER BY stop_count DESC;";

        lowestPrice = "SELECT A.Route_ID, MIN(A.pricepermile * A.distance) as price\n" +
                "  FROM (SELECT R.route_id, pricepermile, distance, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "      INTERSECT\n" +
                "    SELECT R.route_id, pricepermile, distance, weekday, seats_taken, seats\n" +
                "      FROM schedule S\n" +
                "      INNER JOIN train T\n" +
                "      ON S.Train_ID = T.train_id\n" +
                "      INNER JOIN route_stop RS\n" +
                "      ON S.Route_ID = RS.Route_ID\n" +
                "      INNER JOIN stop\n" +
                "      ON RS.Stop_ID = stop.Stop_ID\n" +
                "      INNER JOIN route R\n" +
                "      ON RS.Route_ID = R.route_id\n" +
                "      WHERE\n" +
                "        Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "    ) as A\n" +
                "    WHERE weekday = ? AND seats_taken < seats\n" +
                "    GROUP BY A.route_id" +
                "    ORDER BY price ASC;\n;";

        highestPrice = "SELECT A.Route_ID, MAX(A.pricepermile * A.distance) as price\n" +
                "  FROM (SELECT R.route_id, pricepermile, distance, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "      INTERSECT\n" +
                "    SELECT R.route_id, pricepermile, distance, weekday, seats_taken, seats\n" +
                "      FROM schedule S\n" +
                "      INNER JOIN train T\n" +
                "      ON S.Train_ID = T.train_id\n" +
                "      INNER JOIN route_stop RS\n" +
                "      ON S.Route_ID = RS.Route_ID\n" +
                "      INNER JOIN stop\n" +
                "      ON RS.Stop_ID = stop.Stop_ID\n" +
                "      INNER JOIN route R\n" +
                "      ON RS.Route_ID = R.route_id\n" +
                "      WHERE\n" +
                "        Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "    ) as A\n" +
                "    WHERE weekday = ? AND seats_taken < seats\n" +
                "    GROUP BY A.route_id" +
                "    ORDER BY price DESC;\n;";

        leastTime = "SELECT A.Route_ID, MIN((A.distance * 60)/A.topspeed) as time\n" +
                "FROM (SELECT R.route_id, pricepermile, distance, topspeed, weekday, seats_taken, seats\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  ON S.Train_ID = T.train_id\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON S.Route_ID = RS.Route_ID\n" +
                "  INNER JOIN stop\n" +
                "  ON RS.Stop_ID = stop.Stop_ID\n" +
                "  INNER JOIN route R\n" +
                "  ON RS.Route_ID = R.route_id\n" +
                "  WHERE\n" +
                "    Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "    INTERSECT\n" +
                "  SELECT R.route_id, pricepermile, distance, topspeed, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "  ) as A\n" +
                "  WHERE weekday = ? AND seats_taken < seats\n" +
                "  GROUP BY A.route_id" +
                "  ORDER BY time ASC;";

        mostTime = "SELECT A.Route_ID, MAX((A.distance * 60)/A.topspeed) as time\n" +
                "FROM (SELECT R.route_id, pricepermile, distance, topspeed, weekday, seats_taken, seats\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  ON S.Train_ID = T.train_id\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON S.Route_ID = RS.Route_ID\n" +
                "  INNER JOIN stop\n" +
                "  ON RS.Stop_ID = stop.Stop_ID\n" +
                "  INNER JOIN route R\n" +
                "  ON RS.Route_ID = R.route_id\n" +
                "  WHERE\n" +
                "    Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "    INTERSECT\n" +
                "  SELECT R.route_id, pricepermile, distance, topspeed, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "  ) as A\n" +
                "  WHERE weekday = ? AND seats_taken < seats\n" +
                "  GROUP BY A.route_id" +
                "  ORDER BY time DESC;";

        leastDistance = "SELECT A.Route_ID, MIN(distance) as distance\n" +
                "FROM (SELECT R.route_id, distance, weekday, seats_taken, seats\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  ON S.Train_ID = T.train_id\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON S.Route_ID = RS.Route_ID\n" +
                "  INNER JOIN stop\n" +
                "  ON RS.Stop_ID = stop.Stop_ID\n" +
                "  INNER JOIN route R\n" +
                "  ON RS.Route_ID = R.route_id\n" +
                "  WHERE\n" +
                "    Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "    INTERSECT\n" +
                "  SELECT R.route_id, distance, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "  ) as A\n" +
                "  WHERE weekday = ? AND seats_taken < seats\n" +
                "  GROUP BY A.Route_ID" +
                "  ORDER BY distance ASC;";

        mostDistance = "SELECT A.Route_ID, MAX(distance) as distance\n" +
                "FROM (SELECT R.route_id, distance, weekday, seats_taken, seats\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  ON S.Train_ID = T.train_id\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON S.Route_ID = RS.Route_ID\n" +
                "  INNER JOIN stop\n" +
                "  ON RS.Stop_ID = stop.Stop_ID\n" +
                "  INNER JOIN route R\n" +
                "  ON RS.Route_ID = R.route_id\n" +
                "  WHERE\n" +
                "    Station_A_ID = ? AND Stops_At_A = TRUE\n" +
                "    INTERSECT\n" +
                "  SELECT R.route_id, distance, weekday, seats_taken, seats\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_B_ID = ? AND Stops_At_B = TRUE\n" +
                "  ) as A\n" +
                "  WHERE weekday = ? AND seats_taken < seats\n" +
                "  GROUP BY A.Route_ID" +
                "  ORDER BY distance DESC;";

        //end aggregate functions

        combinationStop1 = "SELECT R.route_id\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_A_ID = ? AND Stops_At_A = TRUE AND weekday = ? AND R.route_id not in \n" +
                "          (SELECT R.route_id\n" +
                "          FROM schedule S\n" +
                "          INNER JOIN train T\n" +
                "          ON S.Train_ID = T.train_id\n" +
                "          INNER JOIN route_stop RS\n" +
                "          ON S.Route_ID = RS.Route_ID\n" +
                "          INNER JOIN stop\n" +
                "          ON RS.Stop_ID = stop.Stop_ID\n" +
                "          INNER JOIN route R\n" +
                "          ON RS.Route_ID = R.route_id\n" +
                "          WHERE\n" +
                "            Station_B_ID = ? AND Stops_At_B = TRUE AND weekday = ?);";

        combinationStop2 = "SELECT station_b_id FROM(\n" +
                "  WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "    SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "    FROM route_stop, stop\n" +
                "    WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "  UNION ALL\n" +
                "    SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "    FROM sortroute sr, route_stop rs, stop s\n" +
                "    WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "  )\n" +
                "  SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "WHERE row > (\n" +
                "  SELECT row FROM(\n" +
                "    WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" +
                "      SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" +
                "      FROM route_stop, stop\n" +
                "      WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" +
                "    UNION ALL\n" +
                "      SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" +
                "      FROM sortroute sr, route_stop rs, stop s\n" +
                "      WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" +
                "    )\n" +
                "    SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" +
                "  WHERE station_A_id = ?) AND stops_at_b = TRUE;";

        combinationStop3 = "SELECT R.route_id\n" +
                "    FROM schedule S\n" +
                "    INNER JOIN train T\n" +
                "    ON S.Train_ID = T.train_id\n" +
                "    INNER JOIN route_stop RS\n" +
                "    ON S.Route_ID = RS.Route_ID\n" +
                "    INNER JOIN stop\n" +
                "    ON RS.Stop_ID = stop.Stop_ID\n" +
                "    INNER JOIN route R\n" +
                "    ON RS.Route_ID = R.route_id\n" +
                "    WHERE\n" +
                "      Station_B_ID = ? AND Stops_At_B = TRUE AND weekday = ? AND R.route_id not in \n" +
                "          (SELECT R.route_id\n" +
                "          FROM schedule S\n" +
                "          INNER JOIN train T\n" +
                "          ON S.Train_ID = T.train_id\n" +
                "          INNER JOIN route_stop RS\n" +
                "          ON S.Route_ID = RS.Route_ID\n" +
                "          INNER JOIN stop\n" +
                "          ON RS.Stop_ID = stop.Stop_ID\n" +
                "          INNER JOIN route R\n" +
                "          ON RS.Route_ID = R.route_id\n" +
                "          WHERE\n" +
                "            Station_A_ID = ? AND Stops_At_A = TRUE AND weekday = ?);";

        combinationStop4 = "SELECT station_a_id FROM(\n" + 
                "  WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" + 
                "    SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" + 
                "    FROM route_stop, stop\n" + 
                "    WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" + 
                "  UNION ALL\n" + 
                "    SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" + 
                "    FROM sortroute sr, route_stop rs, stop s\n" + 
                "    WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" + 
                "  )\n" + 
                "  SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" + 
                "WHERE row <= (\n" + 
                "  SELECT row FROM(\n" + 
                "    WITH RECURSIVE sortroute(route_id, stops_at_a, stops_at_b, stop_id, station_a_id, station_b_id) AS (\n" + 
                "      SELECT route_id, stops_at_a, stops_at_b, stop.stop_id AS stop_id, station_a_id, station_b_id\n" + 
                "      FROM route_stop, stop\n" + 
                "      WHERE route_stop.stop_id = stop.stop_id AND stop.stop_id = (SELECT stop_id FROM route WHERE route_id = ?) AND route_id = ?\n" + 
                "    UNION ALL\n" + 
                "      SELECT rs.route_id, rs.stops_at_a, rs.stops_at_b, s.stop_id, s.station_a_id, s.station_b_id\n" + 
                "      FROM sortroute sr, route_stop rs, stop s\n" + 
                "      WHERE (rs.stop_id = s.stop_id AND rs.route_id = sr.route_id AND sr.station_b_id = s.station_a_id)\n" + 
                "    )\n" + 
                "    SELECT *, ROW_NUMBER () OVER () AS row FROM sortroute) AS A\n" + 
                "  WHERE station_b_id = ?) AND stops_at_a = TRUE;";



        addReservation = "UPDATE schedule SET seats_taken = seats_taken + 1\n" +
                "\tWHERE weekday = ? AND runtime = ? AND Route_ID = ?" +
                "RETURNING seats_taken;";

        //advanced searches

        specificStationDayTime = "SELECT distinct T.train_id\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  ON S.Train_ID = T.train_id\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON S.Route_ID = RS.Route_ID\n" +
                "  INNER JOIN stop\n" +
                "  ON RS.Stop_ID = stop.Stop_ID\n" +
                "  WHERE\n" +
                "    (Station_A_ID = ? OR Station_B_ID = ?) AND weekday = ? AND runtime = ?\n" +
                "  ORDER BY T.train_id ASC;\n";

        moreThanOneRail = "SELECT Route_ID\n" +
                "  FROM railline_route\n" +
                "  GROUP BY Route_ID having count(*) > 1\n" +
                "  ORDER BY Route_ID ASC;";

        sameStationDifferentStop = "SELECT route_id\n" +
                "  FROM (SELECT route_id, count(route_id) AS Count, count(CASE WHEN stops_at_b THEN 1 END) AS stopCount FROM route_stop\n" +
                "    WHERE route_id NOT IN ( SELECT route_id FROM (\n" +
                "      (SELECT route_id, stop_id FROM (SELECT stop_id FROM route_stop WHERE route_id = ? ) AS stops CROSS JOIN\n" +
                "        (SELECT DISTINCT route_id FROM route_stop) AS routes)\n" +
                "      EXCEPT\n" +
                "        (SELECT route_id , stop_id FROM route_stop) ) AS existing )\n" +
                "      GROUP BY route_id) AS temp\n" +
                "  WHERE temp.count = (SELECT COUNT(*) FROM route_stop where route_id = ?)\n" +
                "  AND temp.stopCount <> (SELECT COUNT(CASE WHEN stops_at_b THEN 1 END) FROM route_stop where route_id = ?);";

        allTrainsPass = "SELECT station_id FROM (\n" +
                "\tSELECT station_id, COUNT(train_id) FROM (\n" +
                "\t\tSELECT DISTINCT train_id, station_a_id AS station_id from schedule, stop, route_stop WHERE schedule.route_id = route_stop.route_id AND route_stop.stop_id = stop.stop_id UNION SELECT DISTINCT train_id, station_b_id AS station_id from schedule, stop, route_stop WHERE schedule.route_id = route_stop.route_id AND route_stop.stop_id = stop.stop_id\n" +
                "\t\t) AS temp\n" +
                "\tGROUP BY station_id) AS amount\n" +
                "\tWHERE count = (SELECT COUNT(*) FROM train);";

        doNotStopAtStation = "SELECT DISTINCT train_id as id\n" +
                "\tFROM schedule\n" +
                "    EXCEPT\n" +
                "\tSELECT DISTINCT S.Train_ID as id\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  ON S.Train_ID = T.train_id\n" +
                "  INNER JOIN route_stop RS\n" +
                "  ON S.Route_ID = RS.Route_ID\n" +
                "  INNER JOIN stop\n" +
                "  ON RS.Stop_ID = stop.Stop_ID\n" +
                "  WHERE\n" +
                "    (Station_A_ID = ? AND Stops_At_A = TRUE) OR (Station_B_ID = ? AND Stops_At_A = TRUE);";

        percentStops = "SELECT stats.route_id FROM (\n" +
                "  SELECT route_id, COUNT(route_id) + 1 AS stations, (COUNT(CASE WHEN stops_at_b THEN 1 END) + 1) * 100 AS stops\n" +
                "    FROM route_stop\n" +
                "    GROUP BY route_id)\n" +
                "  AS stats\n" +
                "  WHERE stats.stops / stats.stations >= (? * 100)  order by route_id asc;";

        routeSchedule = "SELECT weekday, runtime, Train_ID\n" +
                "  FROM schedule\n" +
                "  WHERE Route_ID = ?;";

        availableDayTime = "SELECT (T.seats - S.seats_taken) AS availability\n" +
                "  FROM schedule S\n" +
                "  INNER JOIN train T\n" +
                "  on S.TRAIN_ID = T.train_id\n" +
                "  WHERE S.Route_ID = ? AND S.weekday = ? AND S.runtime = ?;";
    }
}
