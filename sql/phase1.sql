DROP TABLE IF EXISTS station CASCADE;
DROP TABLE IF EXISTS railline CASCADE;
DROP TABLE IF EXISTS route CASCADE;
DROP TABLE IF EXISTS schedule CASCADE;
DROP TABLE IF EXISTS train CASCADE;
DROP TABLE IF EXISTS passenger CASCADE;

CREATE TABLE station (
  station_id      INT,
  address         VARCHAR(100),
  opentime        TIME NOT NULL,
  closetime       TIME NOT NULL,
  PRIMARY KEY(station_id)
);

CREATE TABLE railline (
  railline_id     INT,
  speedlimit      INT,
  PRIMARY KEY(railline_id)
);

CREATE TABLE station_railline (
  PRIMARY KEY(Station_ID, Railline_ID),
  FOREIGN KEY (Station_ID) REFERENCES station(station_id),
  FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id)
);

CREATE TABLE route (
  route_id        INT,
  PRIMARY KEY(route_id)
);

CREATE TABLE railline_route (
  PRIMARY KEY(Railline_ID, Route_ID),
  FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),
  FOREIGN KEY (Route_ID) REFERENCES route(route_id)
);

CREATE TABLE schedule (
 schedule_id      INT,
 PRIMARY KEY (schedule_id),

 CONSTRAINT Schedule_Route_FK
  FOREIGN KEY (Route_ID) REFERENCES route(route_id),

 CONSTRAINT Schedule_Train_FK
  FOREIGN KEY (Train_ID) REFERENCES train(train_id)
);

CREATE TABLE train (
  train_id        INT,
  topspeed        INT,
  seats           INT,
  pricepermile    DECIMAL(4,2),
  PRIMARY KEY(train_id)
);

CREATE TABLE passenger (
  passenger_id    SERIAL,
  firstname       VARCHAR(20),
  lastname        VARCHAR(20),
  phone           VARCHAR(15),
  street          VARCHAR(50),
  city            VARCHAR(50),
  state           VARCHAR(25),
  zip             VARCHAR(15),
  country         VARCHAR(20),
  email           VARCHAR(35),
  PRIMARY KEY(passenger_id)
);

CREATE TABLE train_passenger (
  PRIMARY KEY(Train_ID, Passenger_ID)
  FOREIGN KEY (Train_ID) REFERENCES train(train_id),
  FOREIGN KEY (Passenger_ID) REFERENCES passenger(passenger_id)
);

CREATE TABLE stop (
  FOREIGN KEY (Station_A_ID) REFERENCES station(station_id),
  FOREIGN KEY (Station_B_ID) REFERENCES station(station_id),
  distancebetween  INT,
  PRIMARY KEY(Station_A_ID, Station_B_ID)
);

CREATE TABLE railline_stop (
  PRIMARY KEY(StopA_ID, StopB_ID, Railline_ID),
  FOREIGN KEY (StopA_ID) REFERENCES stop(Station_A_ID),
  FOREIGN KEY (StopB_ID) REFERENCES railline(Station_B_ID),
  FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),
);
