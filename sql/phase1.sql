DROP TABLE IF EXISTS station CASCADE;
DROP TABLE IF EXISTS railline CASCADE;
DROP TABLE IF EXISTS route CASCADE;
DROP TABLE IF EXISTS schedule CASCADE;
DROP TABLE IF EXISTS train CASCADE;
DROP TABLE IF EXISTS passenger CASCADE;
DROP TABLE IF EXISTS stop CASCADE;
DROP TABLE IF EXISTS route_stop CASCADE;
DROP TABLE IF EXISTS station_railline CASCADE;
DROP TABLE IF EXISTS railline_route CASCADE;

CREATE TABLE station (
  station_id      SERIAL,
  address         VARCHAR(100) UNIQUE,
  opentime        TIME NOT NULL,
  closetime       TIME NOT NULL,
  distance        INT,
  name            VARCHAR(50) UNIQUE,
  CONSTRAINT Station_PK
    PRIMARY KEY(station_id)
);

CREATE TABLE railline (
  railline_id     SERIAL,
  speedlimit      INT,
  CONSTRAINT Railline_PK
    PRIMARY KEY(railline_id)
);

CREATE TABLE station_railline (
  Station_ID      INT NOT NULL,
  Railline_ID     INT NOT NULL,

  CONSTRAINT Station_FK
    FOREIGN KEY (Station_ID) REFERENCES station(station_id),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),

  CONSTRAINT Station_Railline_PK
    PRIMARY KEY(Station_ID, Railline_ID)
);

CREATE TABLE passenger (
  passenger_id    SERIAL,
  firstname       VARCHAR(20),
  lastname        VARCHAR(20),
  phone           VARCHAR(20),
  street          VARCHAR(50),
  city            VARCHAR(50),
  state           VARCHAR(25),
  zip             VARCHAR(15),
  country         VARCHAR(20),
  email           VARCHAR(75),

  CONSTRAINT Passenger_PK
    PRIMARY KEY(passenger_id)
);

CREATE TABLE stop (
  Stop_ID           SERIAL,
  Station_A_ID      INT,
  Station_B_ID      INT,

  CONSTRAINT Station_A_FK
    FOREIGN KEY (Station_A_ID) REFERENCES station(station_id),

  CONSTRAINT Station_B_FK
    FOREIGN KEY (Station_B_ID) REFERENCES station(station_id),

  distancebetween  INT,

  CONSTRAINT Stop_PK
    PRIMARY KEY(Stop_ID),
  CONSTRAINT Unique_Station_Combo
  	UNIQUE (Station_A_ID, Station_B_ID)
);

CREATE TABLE route (
  route_id        SERIAL,
  description     VARCHAR(50),
  Stop_ID         INT,
  distance        INT,

  CONSTRAINT Route_PK
    PRIMARY KEY(route_id),
  CONSTRAINT Stop_FK
    FOREIGN KEY (Stop_ID) REFERENCES stop(Stop_ID)
);

CREATE TABLE train (
  train_id        SERIAL,
  topspeed        INT,
  seats           INT,
  pricepermile    DECIMAL(4,2),

  CONSTRAINT Train_PK
    PRIMARY KEY(train_id)
);

CREATE TABLE schedule (
 schedule_id      SERIAL,
 weekday          VARCHAR(4) NOT NULL,
 runtime          TIME NOT NULL,
 -- primary key is route, week, and time
 -- add train to each schedule
 Route_ID         INT NOT NULL,
 Train_ID         INT NOT NULL,
 seats_taken      INT,

 CONSTRAINT Schedule_PK
  PRIMARY KEY (schedule_id),

 CONSTRAINT Schedule_Route_FK
  FOREIGN KEY (Route_ID) REFERENCES route(route_id),

 CONSTRAINT Schedule_Train_FK
  FOREIGN KEY (Train_ID) REFERENCES train(train_id)
);

CREATE TABLE railline_route (
  Railline_ID     INT NOT NULL,
  Route_ID        INT NOT NULL,

  CONSTRAINT Railline_Route_PK
    PRIMARY KEY(Railline_ID, Route_ID),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),
  CONSTRAINT Route_FK
    FOREIGN KEY (Route_ID) REFERENCES route(route_id)
);

CREATE TABLE route_stop (
  Stop_ID           INT NOT NULL,
  Route_ID          INT NOT NULL,
  Stops_At_A        BOOLEAN,
  Stops_At_B        BOOLEAN,

  CONSTRAINT Route_Stop_PK
    PRIMARY KEY(Stop_ID, Route_ID),
  CONSTRAINT Station_A_FK
    FOREIGN KEY (Stop_ID) REFERENCES stop(Stop_ID),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Route_ID) REFERENCES route(Route_ID)
);

---------------- Add one to seats_taken on insert --------------------

DROP TRIGGER IF EXISTS update_seats ON schedule;

CREATE OR REPLACE FUNCTION seats()
  RETURNS trigger AS
$$
BEGIN
  IF (SELECT seats_taken from schedule where schedule_id = NEW.schedule_id) + 1 >= (SELECT seats from train where train_id = NEW.Train_ID) THEN
    RAISE NOTICE 'train is full';
    RETURN NULL;
  END IF;

  RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER update_seats
  BEFORE update
  ON schedule
  FOR EACH ROW
  EXECUTE PROCEDURE seats();


---------------- Make sure track's empty on insert --------------------
