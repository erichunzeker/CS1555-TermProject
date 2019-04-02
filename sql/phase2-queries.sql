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
