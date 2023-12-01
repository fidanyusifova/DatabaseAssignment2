//import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbFunctions {
    private static final String dbname = "BookStore";
    private static final String user = "postgres";
    private static final String pass = "fidan123";


    public Connection connect_to_db() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }
}