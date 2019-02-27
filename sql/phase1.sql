DROP TABLE IF EXISTS station CASCADE;
DROP TABLE IF EXISTS railline CASCADE;
DROP TABLE IF EXISTS route CASCADE;
DROP TABLE IF EXISTS schedule CASCADE;
DROP TABLE IF EXISTS train CASCADE;
DROP TABLE IF EXISTS passenger CASCADE;
DROP TABLE IF EXISTS station_railline CASCADE;
DROP TABLE IF EXISTS railline_route CASCADE;
DROP TABLE IF EXISTS train_passenger CASCADE;

CREATE TABLE station (
  station_id      INT,
  address         VARCHAR(100),
  opentime        TIME NOT NULL,
  closetime       TIME NOT NULL,
  CONSTRAINT Station_PK
    PRIMARY KEY(station_id)
);

CREATE TABLE railline (
  railline_id     INT,
  speedlimit      INT,
  CONSTRAINT Railline_PK
    PRIMARY KEY(railline_id)
);

CREATE TABLE station_railline (
  Station_ID      INT,
  Railline_ID     INT,

  CONSTRAINT Station_FK
    FOREIGN KEY (Station_ID) REFERENCES station(station_id),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),

  CONSTRAINT Station_Railline_PK
    PRIMARY KEY(Station_ID, Railline_ID)
);

CREATE TABLE route (
  route_id        INT,
  CONSTRAINT Route_PK
    PRIMARY KEY(route_id)
);

CREATE TABLE railline_route (
  Railline_ID     INT,
  Route_ID        INT,

  CONSTRAINT Railline_Route_PK
    PRIMARY KEY(Railline_ID, Route_ID),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),
  CONSTRAINT Route_FK
    FOREIGN KEY (Route_ID) REFERENCES route(route_id)
);

CREATE TABLE train (
  train_id        INT,
  topspeed        INT,
  seats           INT,
  pricepermile    DECIMAL(4,2),

  CONSTRAINT Train_PK
    PRIMARY KEY(train_id)
);

CREATE TABLE schedule (
 schedule_id      INT,
 Route_ID         INT,
 Train_id         INT,

 CONSTRAINT Schedule_PK
  PRIMARY KEY (schedule_id),

 CONSTRAINT Schedule_Route_FK
  FOREIGN KEY (Route_ID) REFERENCES route(route_id),

 CONSTRAINT Schedule_Train_FK
  FOREIGN KEY (Train_ID) REFERENCES train(train_id)
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

  CONSTRAINT Passenger_PK
    PRIMARY KEY(passenger_id)
);

CREATE TABLE train_passenger (
  Train_ID        INT,
  Passenger_ID    INT,

  CONSTRAINT Train_Passenger_PK
    PRIMARY KEY(Train_ID, Passenger_ID),
  CONSTRAINT Train_FK
    FOREIGN KEY (Train_ID) REFERENCES train(train_id),
  CONSTRAINT Passenger_FK
    FOREIGN KEY (Passenger_ID) REFERENCES passenger(passenger_id)
);

CREATE TABLE stop (
  Station_A_ID      INT,
  Station_B_ID      INT,

  CONSTRAINT Station_A_FK
    FOREIGN KEY (Station_A_ID) REFERENCES station(station_id),

  CONSTRAINT Station_B_FK
    FOREIGN KEY (Station_B_ID) REFERENCES station(station_id),

  distancebetween  INT,

  CONSTRAINT Stop_PK
    PRIMARY KEY(Station_A_ID, Station_B_ID)
);

CREATE TABLE railline_stop (
  StopA_ID          INT,
  StopB_ID          INT,
  Railline_ID       INT,

  CONSTRAINT Railline_Stop_PK
    PRIMARY KEY(StopA_ID, StopB_ID, Railline_ID),
  CONSTRAINT Station_A_FK
    FOREIGN KEY (StopA_ID) REFERENCES stop(Station_A_ID),
  CONSTRAINT Station_B_FK
    FOREIGN KEY (StopB_ID) REFERENCES railline(Station_B_ID),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id)
);
