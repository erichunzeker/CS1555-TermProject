-- insert ddl commands here

CREATE TABLE station (
  station_id      INTEGER,
  address         VARCHAR,

);

CREATE TABLE railline (
  line_id         INTEGER,

);

CREATE TABLE route ();

CREATE TABLE schedule ();

CREATE TABLE train (

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

----------------------------
