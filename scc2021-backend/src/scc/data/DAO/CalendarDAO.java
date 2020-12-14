package scc.data.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import scc.data.Calendar;
import scc.data.JDBCConnection;
import scc.data.Reservation;

public class CalendarDAO implements DAO<Calendar, Long> {
	
    private static final String CALENDARS = "calendars";

    public CalendarDAO() {
        // create table
        create();
    }
	
    private void create() {        
        String query = "CREATE TABLE IF NOT EXISTS " + CALENDARS +
                        " (id TEXT PRIMARY KEY," +
                        " name TEXT," + 
                        " availableDays LIST<DATE>,"+ 		// TODO
                        " calendarEntry MAP<DATE,TEXT>,"+		// TODO
                        " entityId TEXT)";

        try (Connection con = JDBCConnection.getConnection()) {
            //PreparedStatement pst = con.prepareStatement(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private Calendar findCalendar(Long id) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + CALENDARS + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id.toString());
			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			if (rs.next()) {	
				String rid = rs.getString("id"); 
				String name = rs.getString("name");
                String entityId = rs.getString("entityId");
                
				// TODO available Days and calendarEntry
                	
				rs.close();
				stmt.close();
				conn.close();
				
                Calendar calendar = new Calendar();
                calendar.setId(rid.toString());
                calendar.setName(name);
                calendar.setEntityId(entityId);
				return calendar;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Calendar> get(Long id) {
        Calendar calendar = findCalendar(id);
        if (calendar == null)
            return Optional.empty();
        return Optional.of(calendar);
    }

    public Collection<Calendar> getAll() {
        return null;
    }

    public Optional<Long> save(Calendar calendar) {
        return null;
    }

    public void update(Calendar calendar){}

    public void delete(Calendar calendar){}
    
}
