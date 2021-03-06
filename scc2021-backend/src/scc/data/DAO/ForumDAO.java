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
        				" (id TEXT PRIMARY KEY," +
        				" entityId TEXT," +
        				" messageIds TEXT[])";

        String msgQuery = "CREATE TABLE IF NOT EXISTS " + MESSAGES +
							" (id TEXT PRIMARY KEY," +
							" forumId TEXT," +
							" msg TEXT," +
							" fromWho TEXT," +
							" replyToId TEXT)";

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

    @Override
    public Optional<Forum> get(Long id) {
    	Forum forum = getForum(id);
        return forum != null ? Optional.of(forum): Optional.empty();
    }

    @Override
    public Collection<Forum> getAll() {
        return getForums();
    }

    @Override
	public Optional<Long> save(Forum forum) { 
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "INSERT INTO " + FORUMS +
                    	" (id, entityId, messageIds) VALUES (?, ?, ?)";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, forum.getId().toString());
			stmt.setString(2, forum.getEntityId());
			// prepare message ids array
			List<Message> messages = forum.getMessages();
			String[] messageIds = new String[messages.size()];
			for (int i = 0; i < messages.size(); i++) 
				messageIds[i] = messages.get(i).getId(); 
			// put message ids array into query
			stmt.setArray(3, conn.createArrayOf("TEXT", messageIds));
			// execute insert
			int rows = stmt.executeUpdate();
			
			// insert messages into messages table
			for (Message msg: forum.getMessages()) {
				String query = "INSERT INTO " + MESSAGES +
						" (id, forumId, msg, fromWho, replyToId) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement st = conn.prepareStatement(query);
				st.setString(1, msg.getId().toString());
				st.setString(2, msg.getForumId());
				st.setString(3, msg.getMsg());
				st.setString(4, msg.getFromWho());
				st.setString(5, msg.getReplyToId());
				// execute insert message
				rows = st.executeUpdate();
			}
			return Optional.of(Long.parseLong(forum.getId()));
        } catch (Exception e) {
        	e.printStackTrace();
        	return Optional.empty();
        }
    }

    @Override
    public void update(Forum forum) { 
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "UPDATE " + FORUMS +
                    	" SET (id, entityId, messageIds) = (?, ?, ?) WHERE id=?";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, forum.getId().toString());
			stmt.setString(2, forum.getEntityId());
			// prepare message ids array
			List<Message> messages = forum.getMessages();
			String[] messageIds = new String[messages.size()];
			for (int i = 0; i < messages.size(); i++) 
				messageIds[i] = messages.get(i).getId(); 
			// put message ids array into query
			stmt.setArray(3, conn.createArrayOf("TEXT", messageIds));
			stmt.setString(4, forum.getId());
			// execute update
			int rows = stmt.executeUpdate();
			
			// update messages to messages table
			for (Message msg: forum.getMessages()) {
				String query = "UPDATE " + MESSAGES +
						" (id, forumId, msg, fromWho, replyToId) = (?, ?, ?, ?, ?) WHERE id=?";

				PreparedStatement st = conn.prepareStatement(query);
				st.setString(1, msg.getId().toString());
				st.setString(2, msg.getForumId());
				st.setString(3, msg.getMsg());
				st.setString(4, msg.getFromWho());
				st.setString(5, msg.getReplyToId());
				st.setString(5, msg.getId());
				// execute insert message
				rows = st.executeUpdate();
			}	
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void delete(Forum forum) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "DELETE * FROM " + FORUMS + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, forum.getId().toString());
			// delete forum from forums table
			int rows = stmt.executeUpdate();		
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public Collection<Forum> getForumByEntity(String entityId) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + FORUMS + " WHERE entityId=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, entityId);	
			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			List<Forum> forums = new ArrayList<Forum>();
			while (rs.next()) {	
				
				String rid = rs.getString("id"); 
                String rEntityId = rs.getString("entityId"); 
                // get result set of message ids from forums table
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
                forum.setEntityId(rEntityId);
                forum.setMessages(messages);
                
				forums.add(forum);
			}
			return forums;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
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
                // get result set of message ids from forums table
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
                // get result set of message ids from forums table
                ResultSet rMsgIds = rs.getArray(3).getResultSet();
                // add message ids to a list
				List<String> listMsgIds = new ArrayList<String>();
				while(rMsgIds.next()) 
					listMsgIds.add(rMsgIds.getString(1));	
				
				// get messages from messages table
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
				forums.add(forum);
            }
            return forums;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
	}

}
