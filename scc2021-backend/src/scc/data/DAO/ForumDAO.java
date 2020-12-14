package scc.data.DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

import scc.data.Forum;
import scc.data.JDBCConnection;

public class ForumDAO implements DAO<Forum, Long> {
	
    private static final String FORUMS = "forums";
	
    public ForumDAO() {
        // create table
        create();
    }
	
	   private void create() {
	        String query = "CREATE TABLE IF NOT EXISTS " + FORUMS + " (id TEXT)," + " (entityId TEXT),"
	                + " (messages MESSAGE[])";

	        try (Connection con = JDBCConnection.getConnection()) {
	            // PreparedStatement pst = con.prepareStatement(query);
	            Statement stmt = con.createStatement();
	            stmt.executeUpdate(query);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }

    public Optional<Forum> get(Long id) {
        return null;
    }

    public Collection<Forum> getAll() {
        return null;
    }

    public Optional<Long> save(Forum forum) {
        return null;
    }

    public void update(Forum forum) {
    }

    public void delete(Forum forum) {
    }

}
