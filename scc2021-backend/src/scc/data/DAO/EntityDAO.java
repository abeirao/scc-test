package scc.data.DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

import scc.data.Entity;
import scc.data.JDBCConnection;

public class EntityDAO implements DAO<Entity, Long> {

    private static final String ENTITIES = "entities";

    public EntityDAO() {
        // create table
        create();
    }

    private void create() {
        String query = "CREATE TABLE IF NOT EXISTS " + ENTITIES + " (id TEXT)," + " (name TEXT),"
                + " (description TEXT)," + " (mediaIds TEXT[])," + " (calendarId TEXT)," + " (listed BOOLEAN)";

        try (Connection con = JDBCConnection.getConnection()) {
            // PreparedStatement pst = con.prepareStatement(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Optional<Entity> get(Long id) {
        return null;
    }

    public Collection<Entity> getAll() {
        return null;
    }

    public Optional<Long> save(Entity entity) {
        return null;
    }

    public void update(Entity entity) {
    }

    public void delete(Entity entity) {
    }

}
