package scc.data.DAO;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import scc.data.Entity;
import scc.data.JDBCConnection;
import scc.data.Reservation;

public class EntityDAO implements DAO<Entity, Long> {

    private static final String ENTITIES = "entities";

    public EntityDAO() {
        // create table
        create();
    }

    private void create() {
        String query = "CREATE TABLE IF NOT EXISTS " + ENTITIES +
		        		" (id TEXT PRIMARY KEY," + 
		        		" name TEXT," + 
		        		" description TEXT," + 
		        		" mediaIds TEXT[]," + 
		        		" calendarId TEXT," + 
		        		" listed BOOLEAN)";

        try (Connection con = JDBCConnection.getConnection()) {
            // PreparedStatement pst = con.prepareStatement(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<Entity> get(Long id) {
    	Entity entity = getEntity(id);
        return entity != null ? Optional.of(entity): Optional.empty();
    }

    @Override
    public Collection<Entity> getAll() {
        return getEntities();
    }

    @Override
	public Optional<Long> save(Entity entity) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "INSERT INTO " + ENTITIES +
                    	" (id, name, description, mediaIds, calendarId, listed) VALUES (?, ?, ?, ?, ?, ?)";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, entity.getId().toString());
			stmt.setString(2, entity.getName());
			stmt.setString(3, entity.getDescription());
			stmt.setArray(4, conn.createArrayOf("TEXT", entity.getMediaIds()));
			stmt.setString(5, entity.getCalendarId());
			stmt.setBoolean(6, entity.isListed());
			
			int rows = stmt.executeUpdate();
			return Optional.of(Long.parseLong(entity.getId()));
        } catch (Exception e) {
        	e.printStackTrace();
        	return Optional.empty();
        }
    }

    @Override
    public void update(Entity entity) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "UPDATE " + ENTITIES +
				   	" SET (id, name, description, mediaIds, calendarId, listed)=(?, ?, ?, ?, ?, ?) WHERE id=?";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, entity.getId().toString());
			stmt.setString(2, entity.getName());
			stmt.setString(3, entity.getDescription());
			stmt.setArray(4, conn.createArrayOf("String", entity.getMediaIds()));
			stmt.setString(5, entity.getCalendarId());
			stmt.setBoolean(6, entity.isListed());
			stmt.setString(7, entity.getId());
			
			int rows = stmt.executeUpdate();
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void delete(Entity entity) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "DELETE * FROM " + ENTITIES + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, entity.getId().toString());
			
			int rows = stmt.executeUpdate();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    

    private Entity getEntity(Long id) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + ENTITIES + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id.toString());
			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			if (rs.next()) {	
				String rid = rs.getString("id"); // get by column label
				String name = rs.getString("name"); 
				String description = rs.getString("description"); 
				String calendarId = rs.getString("calendarId"); 
				boolean listed = rs.getBoolean("listed"); 
				ResultSet rMediaIds = rs.getArray(4).getResultSet();
				List<String> listMediaIds = new ArrayList<String>();
				while(rMediaIds.next()) 
					listMediaIds.add(rMediaIds.getString(1));
				String[] mediaIds = new String[listMediaIds.size()];
				mediaIds = listMediaIds.toArray(mediaIds);
							
				rs.close();
				stmt.close();
				conn.close();
				
				Entity entity = new Entity();
				entity.setId(rid.toString());
				entity.setName(name);
				entity.setDescription(description);
				entity.setCalendarId(calendarId);
				entity.setListed(listed);
				entity.setMediaIds(mediaIds);
				
				return entity;
            }
            return null;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    private List<Entity> getEntities() {
    	List<Entity> entities = new LinkedList<Entity>();
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + ENTITIES + "";
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			while (rs.next()) {	
				String rid = rs.getString("id"); // get by column label
				String name = rs.getString("name"); 
				String description = rs.getString("description"); 
				String calendarId = rs.getString("calendarId"); 
				boolean listed = rs.getBoolean("listed"); 
				ResultSet rMediaIds = rs.getArray(4).getResultSet();
				List<String> listMediaIds = new ArrayList<String>();
				while(rMediaIds.next()) 
					listMediaIds.add(rMediaIds.getString(1));
				String[] mediaIds = new String[listMediaIds.size()];
				mediaIds = listMediaIds.toArray(mediaIds);
				
				Entity entity = new Entity();
				entity.setId(rid.toString());
				entity.setName(name);
				entity.setDescription(description);
				entity.setCalendarId(calendarId);
				entity.setListed(listed);
				entity.setMediaIds(mediaIds);
				
				entities.add(entity);
            }
			rs.close();
			stmt.close();
			conn.close();
	    	return entities;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
	}


}
