DROP TABLE IF EXISTS station CASCADE;
DROP TABLE IF EXISTS railline CASCADE;
DROP TABLE IF EXISTS route CASCADE;
DROP TABLE IF EXISTS schedule CASCADE;
DROP TABLE IF EXISTS train CASCADE;
DROP TABLE IF EXISTS passenger CASCADE;
DROP TABLE IF EXISTS stop CASCADE;
DROP TABLE IF EXISTS railline_stop CASCADE;
DROP TABLE IF EXISTS station_railline CASCADE;
DROP TABLE IF EXISTS railline_route CASCADE;
DROP TABLE IF EXISTS train_passenger CASCADE;

CREATE TABLE station (
  station_id      INT,
  address         VARCHAR(100),
  opentime        TIME NOT NULL,
  closetime       TIME NOT NULL,
  distance        INT,
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
  description     VARCHAR(50),
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
    PRIMARY KEY(train_id),

  CONSTRAINT Train_Schedule_FK
    FOREIGN KEY (Schedule_ID) REFERENCES schedule(schedule_id)
);

CREATE TABLE schedule (
 schedule_id      INT,
 Route_ID         INT,
 Train_id         INT,

 CONSTRAINT Schedule_PK
  PRIMARY KEY (schedule_id),

 CONSTRAINT Schedule_Route_FK
  FOREIGN KEY (Route_ID) REFERENCES route(route_id)
);

CREATE TABLE agent (
  agent_id        INT,
  name            VARCHAR(30)
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

CREATE TABLE schedule_passenger (
  Schedule_ID     INT,
  Passenger_ID    INT,

  CONSTRAINT Schedule_Passenger_PK
    PRIMARY KEY(Schedule_ID, Passenger_ID),
  CONSTRAINT Train_FK
    FOREIGN KEY (Schedule_ID) REFERENCES schedule(schedule_id),
  CONSTRAINT Passenger_FK
    FOREIGN KEY (Passenger_ID) REFERENCES passenger(passenger_id)
);

CREATE TABLE agent_passenger (
  Agent_ID        INT,
  Passenger_ID    INT,

  CONSTRAINT Agent_Passenger_PK
    PRIMARY KEY(Agent_ID, Passenger_ID),
  CONSTRAINT Agent_FK
    FOREIGN KEY (Agent_ID) REFERENCES agent(agent_id),
  CONSTRAINT Passenger_FK
    FOREIGN KEY (Passenger_ID) REFERENCES passenger(passenger_id)
);

CREATE TABLE stop (
  Stop_ID           INT,
  Station_A_ID      INT,
  Station_B_ID      INT,

  CONSTRAINT Station_A_FK
    FOREIGN KEY (Station_A_ID) REFERENCES station(station_id),

  CONSTRAINT Station_B_FK
    FOREIGN KEY (Station_B_ID) REFERENCES station(station_id),

  distancebetween  INT,

  CONSTRAINT Stop_PK
    PRIMARY KEY(Stop_ID)
);

CREATE TABLE route_stop (
  Stop_ID           INT,
  Route_ID          INT,

  CONSTRAINT Railline_Stop_PK
    PRIMARY KEY(Stop_ID, Route_ID),
  CONSTRAINT Station_A_FK
    FOREIGN KEY (Stop_ID) REFERENCES stop(Stop_ID),
  CONSTRAINT Railline_FK
    FOREIGN KEY (Route_ID) REFERENCES route(Route_ID)
);
