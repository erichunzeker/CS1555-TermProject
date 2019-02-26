-- insert ddl commands here

CREATE TABLE station (
  station_id      INTEGER PRIMARY KEY,
  address         VARCHAR(100),
  opentime        TIME NOT NULL,
  closetime       TIME NOT NULL
);

CREATE TABLE railline (
  railline_id     INTEGER PRIMARY KEY,
  speedlimit      INTEGER
);

CREATE TABLE station_railline (
  CONSTRAINT Station_FK
    FOREIGN KEY (Station_ID) REFERENCES station(station_id),

  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id)
);

CREATE TABLE route (
  route_id        INTEGER PRIMARY KEY
);

CREATE TABLE railline_route (
  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id),

  CONSTRAINT Route_FK
    FOREIGN KEY (Route_ID) REFERENCES route(route_id)
);

CREATE TABLE schedule (
 schedule_id      INTEGER PRIMARY KEY,

 CONSTRAINT Schedule_Route_FK
  FOREIGN KEY (Route_ID) REFERENCES route(route_id),

 CONSTRAINT Schedule_Train_FK
  FOREIGN KEY (Train_ID) REFERENCES train(train_id)
);

CREATE TABLE train (
  train_id        INTEGER PRIMARY KEY,
  topspeed        INTEGER,
  seats           INTEGER,
  pricepermile    DECIMAL(4,2)
);

CREATE TABLE passenger (
  passenger_id    INTEGER PRIMARY KEY,
  firstname       VARCHAR(30),
  lastname        VARCHAR(30),
  phone           VARCHAR(15),
  street          VARCHAR(50),
  city            VARCHAR(50),
  state           VARCHAR(25),
  zip             VARCHAR(15),
  country         VARCHAR(20),
  email           VARCHAR(35),

  CONSTRAINT Passengers_PK
    PRIMARY KEY (passenger_id)
);

CREATE TABLE train_passenger (
  CONSTRAINT Train_FK
    FOREIGN KEY (Train_ID) REFERENCES train(train_id),

  CONSTRAINT Passenger_FK
    FOREIGN KEY (Passenger_ID) REFERENCES passenger(passenger_id)
);

CREATE TABLE stop (

);

----------------------------
