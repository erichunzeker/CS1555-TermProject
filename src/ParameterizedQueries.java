public class ParameterizedQueries {

    public String addCustomer, editCustomer, viewCustomer;

    public ParameterizedQueries() {
        //Update customer list
        addCustomer = "INSERT INTO passenger (firstname, lastname, phone, street, city, state, zip, country, email)\n" +
                "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING passenger_id;";

        editCustomer = "UPDATE passenger SET\n" +
                "  (firstname, lastname, phone, street, city, state, zip, country, email) = ('?', '?', '?', '?', '?', '?', '?', '?', '?')\n" +
                "  WHERE passenger_id =?\n" +
                "  RETURNING passenger_id;";

        viewCustomer = "SELECT *\n" +
                "  FROM passenger\n" +
                "  WHERE passenger_id = ?;";

        //Finding a trip between two stations

    }
}
