public class ParameterizedQueries {

    public String addCustomer, editCustomer, viewCustomer;
    public String singleRoute, addReservation;
    public String specificStationDayTime, moreThanOneRail, sameStationDifferentStop, allTrainsPass, doNotStopAtStation, percentStops, routeSchedule, availableDayTime;

    public ParameterizedQueries() {
        //Update customer list
        addCustomer = "INSERT INTO passenger (firstname, lastname, phone, street, city, state, zip, country, email)\n" +
                "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING passenger_id;";

        editCustomer = "UPDATE passenger SET\n" +
                "  (firstname, lastname, phone, street, city, state, zip, country, email) = ('?', '?', '?', '?', '?', '?', '?', '?', '?')\n" +
                "  WHERE passenger_id =?\n" +
                "  RETURNING passenger_id;";

        viewCustomer = "SELECT *\n" +
                "  FROM passenger\n" +
                "  WHERE passenger_id = ?;";

        //Finding a trip between two stations

        singleRoute = "SELECT *\n" +
                "  FROM (SELECT R.route_id, weekday\n" +
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
                "    SELECT R.route_id, weekday\n" +
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
                "    WHERE A.weekday = ?;";

        addReservation = "UPDATE schedule SET seats_taken = seats_taken\n" +
                "\tWHERE weekday = '?' AND runtime = time'?' AND Route_ID = ?;";

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
                "    (Station_A_ID = ? OR Station_B_ID = ?) AND weekday = '?' AND runtime = '?'\n" +
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
                "  WHERE S.Route_ID = ? AND S.weekday = '?' AND S.runtime = '?';";
    }
}
