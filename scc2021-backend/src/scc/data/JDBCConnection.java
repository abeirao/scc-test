package scc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class JDBCConnection {

    private static Optional<Connection> connection = Optional.empty();
    
    private static final String HOSTNAME = "database";

    public static Connection getConnection() {
        if (connection.isEmpty()) {
            String url = "jdbc:postgresql://" + HOSTNAME + ":5432/";
            String user = "postgres";
            String password = "postgres";

            try {
                connection = Optional.ofNullable(
                    DriverManager.getConnection(url));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return connection.get();
    }
    
}