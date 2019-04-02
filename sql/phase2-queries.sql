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
    )
-- 1.2.4. For each of the trip search options listed above, the following
-- sorting options should be allowed. Note that each trip search should produce
-- a paginated list of results (i.e., each trip search show produce 10 results
-- at a time, with the option to grab the next 10 if needed).

-- 1.2.4.1. Fewest stops
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
  ORDER BY COUNT(*) DESC
-- 1.2.4.2. Run through most stations
-- 1.2.4.3. Lowest price
SELECT max(temp_lo) FROM weather;

-- 1.2.4.4. Highest price
-- 1.2.4.5. Least total time
-- 1.2.4.6. Most total time
-- 1.2.4.7. Least total distance
-- 1.2.4.8. Most total distance




-- 1.3.8
-- Find the availability of a route at every stop on a specific day
-- and time: Find the number of available seats at each stop of a
-- route for the day and time given as parameters.

-- 1.3.7
-- Display the schedule of a route: For a specified route, list the days of
-- departure, departure hours and trains that run it.