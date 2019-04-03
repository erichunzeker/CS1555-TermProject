-- 1.1.1: Add Customer
INSERT INTO passenger (firstname, lastname, phone, street, city, state, zip, country, email)
  VALUES ('first', 'last', 'tel', 'street', 'city', 'state', 'zip', 'country', 'email')
  RETURNING passenger_id;

-- 1.1.1: Edit Customer
UPDATE passenger SET
  (firstname, lastname, phone, street, city, state, zip, country, email) = ('first', 'last', 'tel', 'street', 'city', 'state', 'zip', 'country', 'email')
  WHERE passenger_id = 1
  RETURNING passenger_id;

-- 1.1.1: View Customer
SELECT *
  FROM passenger
  WHERE passenger_id = 1;

-- 1.2.1 Single Route Trip Search
SELECT *
  FROM route
  WHERE route_id IN (
    SELECT Route_ID as R
    FROM route_stop
    WHERE Stop_ID IN (
      SELECT Stop_ID
      FROM stop
      WHERE Station_A_ID = 1
      ) AND (
      SELECT Stop_ID
      from stop
      WHERE Station_B_ID = 4
      )) AND (
      SELECT Route_ID
      FROM schedule
      where weekday = 'Wed'
    );

-- 1.2.2 Combination Route Trip Search
-- implement in application layer

-- 1.2.3. Note that all trip searches must account for available seats, and only
-- show results for trains that have available seats.

SELECT *
  FROM schedule
  INNER JOIN train
  ON schedule.Train_ID = train.train_id
  WHERE seats_taken <= seats;


-- 1.2.4. For each of the trip search options listed above, the following
-- sorting options should be allowed. Note that each trip search should produce
-- a paginated list of results (i.e., each trip search show produce 10 results
-- at a time, with the option to grab the next 10 if needed).

-- 1.2.4.1. Fewest stops ( do pagnation in parameterized query in phase 3 )
SELECT route_id, description
  FROM route
  WHERE route_id IN (
    SELECT Route_ID as R
    FROM route_stop
    WHERE Stop_ID IN (
      SELECT Stop_ID
      FROM stop
      WHERE Station_A_ID = 1
      ) UNION ALL (
      SELECT Stop_ID
      from stop
      WHERE Station_B_ID = 4
      ))
  GROUP BY route_id
  ORDER BY COUNT(*) ASC;

-- 1.2.4.2. Run through most stations
SELECT route_id, description
  FROM route
  WHERE route_id IN (
    SELECT Route_ID as R
    FROM route_stop
    WHERE Stop_ID IN (
      SELECT Stop_ID
      FROM stop
      WHERE Station_A_ID = 1
      ) AND (
      SELECT Stop_ID
      from stop
      WHERE Station_B_ID = 4
      ))
  ORDER BY COUNT(*) ASC;

-- 1.2.4.3. Lowest price
-- 1.2.4.4. Highest price
-- 1.2.4.5. Least total time
-- 1.2.4.6. Most total time
-- 1.2.4.7. Least total distance

SELECT station.distance INTO D1 from station where station_id = 1;
SELECT station.distance INTO D2 from station where station_id = 4;

SELECT route_id, description
  FROM route
  WHERE route_id IN (
    SELECT Route_ID as R
    FROM route_stop
    WHERE Stop_ID IN (
      SELECT Stop_ID
      FROM stop
      WHERE Station_A_ID = 1
      ) AND (
      SELECT Stop_ID
      from stop
      WHERE Station_B_ID = 4
      ))
  ORDER BY SUM(*) ASC;



-- 1.2.4.8. Most total distance

-- 1.2.5. Add Reservation: Book a specified passenger along all legs of the specified route(s) on a given day.

INSERT INTO schedule (weekday, runtime, Route_ID)
  VALUES ('Wed', '10:00:00', '4');
  -- route_id is found by asking system 1.2.1 or 1.2.2

-- 1.3.2. Find the routes that travel more than one rail line: Find all routes that travel more than one rail line.

SELECT Route_ID
  FROM railline_route
  GROUP BY Route_ID having count(*) > 1;

-- 1.3.3. Find routes that pass through the same stations but don’t have the same stops: Find seemingly similar routes that differ by at least 1 stop.

SELECT route_id 
  FROM (SELECT route_id, count(route_id) AS Count, count(CASE WHEN stops_at_b THEN 1 END) AS stopCount FROM route_stop
    WHERE route_id NOT IN ( SELECT route_id FROM (
      (SELECT route_id, stop_id FROM (SELECT stop_id FROM route_stop WHERE route_id = 1 ) AS stops CROSS JOIN
        (SELECT DISTINCT route_id FROM route_stop) AS routes)
      EXCEPT
        (SELECT route_id , stop_id FROM route_stop) ) AS existing )
      GROUP BY route_id) AS temp
  WHERE temp.count = (SELECT COUNT(*) FROM route_stop where route_id = 1) 
  AND temp.stopCount <> (SELECT COUNT(CASE WHEN stops_at_b THEN 1 END) FROM route_stop where route_id = 1);

-- 1.3.4. Find any stations through which all trains pass through: Find any stations that all the trains (that are in the system) pass at any time during an entire week.
-- 1.3.5. Find all the trains that do not stop at a specific station: Find all trains that do not stop at a specified station at any time during an entire week.

SELECT *
  FROM schedule S
  INNER JOIN train T
  ON S.Train_ID = T.train_id
  INNER JOIN route_stop RS
  ON S.Route_ID = RS.Route_ID
  INNER JOIN stop
  ON RS.Stop_ID = stop.Stop_ID
  WHERE
    Station_A_ID <> 2 AND Station_B_ID <> 2;

-- 1.3.6. Find routes that stop at least at XX% of the Stations they visit: Find routes where they stop at least in XX% (where XX number from 10 to 90) of the stations from which they pass (e.g., if a route passes through 5 stations and stops at at least 3 of them, it will be returned as a result for a 50% search).


-- 1.3.7 Display the schedule of a route: For a specified route, list the days of departure, departure hours and trains that run it.
SELECT weekday, runtime, Train_ID
  FROM schedule
  WHERE Route_ID = 1;


-- 1.3.8 Find the availability of a route at every stop on a specific day and time: Find the number of available seats at each stop of a route for the day and time given as parameters.
