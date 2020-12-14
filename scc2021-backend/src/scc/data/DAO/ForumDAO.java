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
    
    private Forum findForum(Long id) {
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
                
				ResultSet rss = stmt.executeQuery("select messages from " + FORUMS);		// Gets messages from forum
				List<Messsage> messages = new ArrayList<Messsage>();
				while (rss.next()) {
					Messsage msg = (Messsage) rss.getObject("messages");
					messages.add(msg);
				}
							
				rs.close();
				stmt.close();
				conn.close();
				
                Forum forum = new Forum();
                forum.setId(rid.toString());
                forum.setEntityId(entityId);
                forum.setMessages(messages);
				return forum;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
   
	  

    public Optional<Forum> get(Long id) {
    	Forum forum = findForum(id);
        if (forum == null)
            return Optional.empty();
        return Optional.of(forum);
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
