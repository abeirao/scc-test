package scc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class JDBCConnection {

    private static Optional<Connection> connection = Optional.empty();

    public static Connection getConnection() {
        if (connection.isEmpty()) {
            String url = "jdbc:postgresql://database:5432/";
            String user = "postgres";
            String password = "postgres";

            try {
                connection = Optional.ofNullable(
                    DriverManager.getConnection(url, user, password));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return connection.get();
    }
    
}