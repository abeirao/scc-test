package scc.data.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import scc.data.Forum;
import scc.data.JDBCConnection;
import scc.data.Messsage;

public class ForumDAO implements DAO<Forum, Long> {
	
	private static final String FORUMS = "forums";
	private static final String MESSAGES = "messages";
	
	public ForumDAO() {
	    // create table
	    create();
	}
	
    private void create() {
        String forumQuery = "CREATE TABLE IF NOT EXISTS " + FORUMS +
        				" (id TEXT)," +
        				" (entityId TEXT)," +
        				" (messages TEXT[])";

        String msgQuery = "CREATE TABLE IF NOT EXISTS " + MESSAGES +
							" (id TEXT)," +
							" (forumId TEXT)," +
							" (msg TEXT)" +
							" (fromWho TEXT)," +
							" (replyToId TEXT)";

        try (Connection con = JDBCConnection.getConnection()) {
            // PreparedStatement pst = con.prepareStatement(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(forumQuery);
            Statement st = con.createStatement();
            st.executeUpdate(msgQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }	  

    public Optional<Forum> get(Long id) {
    	Forum forum = getForum(id);
        return forum == null? Optional.empty(): Optional.of(forum);
    }

    public Collection<Forum> getAll() {
        return getForums();
    }



	public Optional<Long> save(Forum forum) {
        return null;
    }

    public void update(Forum forum) {
    }

    public void delete(Forum forum) {
    }
    
    private Forum getForum(Long id) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + FORUMS + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id.toString());
			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			if (rs.next()) {	
				String rid = rs.getString("id"); 
                String entityId = rs.getString("entityId"); 
                
				// TODO messages
				
				rs.close();
				stmt.close();
				conn.close();
				
                Forum forum = new Forum();
                forum.setId(rid.toString());
                forum.setEntityId(entityId);
				return forum;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private Collection<Forum> getForums() {
		// TODO Auto-generated method stub
		return null;
	}

}
