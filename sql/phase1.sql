-- insert ddl commands here

CREATE TABLE station (
  station_id      INTEGER,
  address         VARCHAR(100),
  open            TIME, 
  close           TIME,

  CONSTRAINT Station_PK
    PRIMARY KEY (station_id)
);

CREATE TABLE railline (
  railline_id     INTEGER,
  speedlimit      INTEGER,

  CONSTRAINT Railline_PK
    PRIMARY KEY (railline_id)
);

CREATE TABLE station_railline (
  CONSTRAINT Station_FK
    FOREIGN KEY (Station_ID) REFERENCES station(station_id),

  CONSTRAINT Railline_FK
    FOREIGN KEY (Railline_ID) REFERENCES railline(railline_id)
);

CREATE TABLE route (
  route_id        INTEGER,

  CONSTRAINT Route_PK
    PRIMARY KEY (route_id)
);

CREATE TABLE schedule (
 schedule_id      INTEGER,

 CONSTRAINT Schedule_PK
  PRIMARY KEY (schedule_id),

 CONSTRAINT Schedule_Route_FK
  FOREIGN KEY (Route_ID) REFERENCES route(route_id),

 CONSTRAINT Schedule_Train_FK
  FOREIGN KEY (Train_ID) REFERENCES train(train_id)
);

CREATE TABLE train (
  train_id        INTEGER,
  topspeed        INTEGER,
  seats           INTEGER,
  pricepermile    DECIMAL(4,2),

  CONSTRAINT Train_PK
    PRIMARY KEY (train_id)
);

CREATE TABLE passenger (
  passenger_id    INTEGER,
  firstname       VARCHAR(30),
  lastname        VARCHAR(50),
  phone           VARCHAR(15),
  street          VARCHAR(50),
  city            VARCHAR(50),
  state           VARCHAR(50),
  zip             VARCHAR(50),
  country         VARCHAR(50),
  email           VARCHAR(50),

  CONSTRAINT Passengers_PK
    PRIMARY KEY (passenger_id)
);

CREATE TABLE stop (
);

----------------------------
