public class ParameterizedQueries {
    String addCustomer = "INSERT INTO passenger (firstname, lastname, phone, street, city, state, zip, country, email)\n" +
            "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING passenger_id;";

}
