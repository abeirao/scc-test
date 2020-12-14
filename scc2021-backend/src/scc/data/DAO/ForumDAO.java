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
import scc.data.Message;

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
        				" (messageIds TEXT[])";

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
                ResultSet rMsgIds = rs.getArray(3).getResultSet();
				List<String> listMsgIds = new ArrayList<String>();
				while(rMsgIds.next()) 
					listMsgIds.add(rMsgIds.getString(1));	
				
				List<Message> messages = new ArrayList<Message>();
				for (String msgId: listMsgIds) {
					String query = "SELECT * FROM " + MESSAGES + " WHERE id=?;";
					 
					// the use of PreparedStatement prevents SQL injection 
					PreparedStatement st = conn.prepareStatement(query);
					st.setString(1, msgId);
					
					ResultSet r = st.executeQuery(); // ResultSet is a Cursor
					if (r.next()) {
						String mId = r.getString("id");
						String forumId = r.getString("forumId");
						String msg = r.getString("msg");
						String fromWho = r.getString("fromWho");
						String replyToId = r.getString("replyToId");
						Message message = new Message();
						message.setId(mId);
						message.setForumId(forumId);
						message.setMsg(msg);
						message.setFromWho(fromWho);
						message.setReplyToId(replyToId);
						
						messages.add(message);
					}
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
        	e.printStackTrace();
            return null;
        }
    }
    
    private Collection<Forum> getForums() {
    	
    	try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + FORUMS;
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			List<Forum> forums = new ArrayList<Forum>();
			while (rs.next()) {	
				String rid = rs.getString("id"); 
                String entityId = rs.getString("entityId"); 
                ResultSet rMsgIds = rs.getArray(3).getResultSet();
				List<String> listMsgIds = new ArrayList<String>();
				while(rMsgIds.next()) 
					listMsgIds.add(rMsgIds.getString(1));	
				
				List<Message> messages = new ArrayList<Message>();
				for (String msgId: listMsgIds) {
					String query = "SELECT * FROM " + MESSAGES + " WHERE id=?;";
					 
					// the use of PreparedStatement prevents SQL injection 
					PreparedStatement st = conn.prepareStatement(query);
					st.setString(1, msgId);
					
					ResultSet r = st.executeQuery(); // ResultSet is a Cursor
					if (r.next()) {
						String mId = r.getString("id");
						String forumId = r.getString("forumId");
						String msg = r.getString("msg");
						String fromWho = r.getString("fromWho");
						String replyToId = r.getString("replyToId");
						Message message = new Message();
						message.setId(mId);
						message.setForumId(forumId);
						message.setMsg(msg);
						message.setFromWho(fromWho);
						message.setReplyToId(replyToId);
						
						messages.add(message);
					}
				}
				
				rs.close();
				stmt.close();
				conn.close();
				
                Forum forum = new Forum();
                forum.setId(rid.toString());
                forum.setEntityId(entityId);
                forum.setMessages(messages);
				forums .add(forum);
            }
            return forums;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
	}

}
