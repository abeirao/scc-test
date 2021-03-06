package scc.data.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import scc.data.Calendar;
import scc.data.Forum;
import scc.data.JDBCConnection;
import scc.data.Message;
import scc.data.Reservation;

public class CalendarDAO implements DAO<Calendar, Long> {
	
    private static final String CALENDARS = "calendars";
    
    private static final String AVAILABLEDAYS = "availableDays";
    
    private static final String CALENDAR_ENTRY = "calendarEntry";


    public CalendarDAO() {
        // create table
        create();
    }
	
    private void create() {        
        String query = "CREATE TABLE IF NOT EXISTS " + CALENDARS +
                        " (id TEXT PRIMARY KEY," +
                        " name TEXT," + 
                        " availableDays DATE[],"+ 		
                        " calendarEntry TEXT[],"+		// TODO
                        " entityId TEXT)";
        
        
        String query2 = "CREATE TABLE IF NOT EXISTS " + AVAILABLEDAYS +
        		"(id TEXT PRIMARY KEY," +
                " days TEXT[])";
        
        String query3 = "CREATE TABLE IF NOT EXISTS " + CALENDAR_ENTRY +
		        		"(date PRIMARY KEY," +
		                " reservation TEXT)";
        
        

        try (Connection con = JDBCConnection.getConnection()) {
            //PreparedStatement pst = con.prepareStatement(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            stmt.executeUpdate(query3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public Optional<Calendar> get(Long id) {
        Calendar calendar = getCalendar(id);
        return calendar != null? Optional.of(calendar): Optional.empty();
    }

    @Override
    public Collection<Calendar> getAll() {
        return getCalendars();
    }

    @Override
    public Optional<Long> save(Calendar calendar) {
    	try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "INSERT INTO " + CALENDARS +
                    	" (id, name, availableDays, calendarEntry, entityId ) VALUES (?, ?, ?, ?, ?)";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, calendar.getId().toString());
			stmt.setString(2, calendar.getName());
			stmt.setString(5, calendar.getEntityId());
			
			// prepare availableDaysIds array
			List<Date> availableDays = calendar.getAvailableDays();
			java.sql.Date[] availableDaysArr = new java.sql.Date[availableDays.size()];
			for (int i = 0; i < availableDays.size(); i++) 
				availableDaysArr[i] = new java.sql.Date(availableDays.get(i).getTime());
			
			// put availableDaysIds array into query
			stmt.setArray(3, conn.createArrayOf("DATE", availableDaysArr));
			
			// set calendar entry ids array 
			stmt.setArray(4, conn.createArrayOf("TEXT", calendar.getCalendarEntry().keySet().toArray()));

			// execute insert
			int rows = stmt.executeUpdate();
			
			// insert calendar entries into calendar entry table
			calendar.getCalendarEntry().forEach((key, value) -> {
				try {
					String query = "INSERT INTO " + CALENDAR_ENTRY +
	                	" (date, reservation) VALUES (?, ?)";
				
					PreparedStatement st = conn.prepareStatement(query);
					st.setDate(1, new java.sql.Date(key.getTime()));
					st.setString(2, value);
										
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			});
			
			return Optional.of(Long.parseLong(calendar.getId()));
        } catch (Exception e) {
        	e.printStackTrace();
        	return Optional.empty();
        }
    }

    @Override
    public void update(Calendar calendar){
    	try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "UPDATE " + CALENDARS +
                    	" SET (id, name, availableDays, calendarEntry, entityId)=(?, ?, ?, ?, ?) WHERE id=?";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, calendar.getId().toString());
			stmt.setString(2, calendar.getName());
			stmt.setString(5, calendar.getEntityId());
			stmt.setString(6, calendar.getId());
			
			// prepare availableDaysIds array
			List<Date> availableDays = calendar.getAvailableDays();
			Date[] availableDaysArr = new Date[availableDays.size()];
			for (int i = 0; i < availableDays.size(); i++) 
				availableDaysArr[i] = availableDays.get(i);
			
			// put availableDaysIds array into query
			stmt.setArray(3, conn.createArrayOf("DATE", availableDaysArr));
			
			// set calendar entry ids array 
			stmt.setArray(4, conn.createArrayOf("TEXT", calendar.getCalendarEntry().keySet().toArray()));

			// execute insert
			int rows = stmt.executeUpdate();
			
			// insert calendar entries into calendar entry table
			calendar.getCalendarEntry().forEach((key, value) -> {
				try {
					String query = "UPDATE " + CALENDAR_ENTRY +
	                	" (date, reservation) = (?, ?) WHERE date=?";
				
					PreparedStatement st = conn.prepareStatement(query);
					st.setDate(1, new java.sql.Date(key.getTime()));
					st.setString(2, value);
					st.setDate(3, new java.sql.Date(key.getTime()));
										
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			});			
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }

    @Override
    public void delete(Calendar calendar){
    	
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "DELETE * FROM " + CALENDARS + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, calendar.getId());
			// delete forum from forums table
			int rows = stmt.executeUpdate();		
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }
    
    private Calendar getCalendar(Long id) {
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
                
                // get available days list 
                ResultSet ravailableDays = rs.getArray(3).getResultSet();
				List<Date> availableDays = new ArrayList<Date>();
				while(ravailableDays.next()) 
					availableDays.add(ravailableDays.getDate(1));
				
				/*
				List<Date> availableDays = new ArrayList<Date>();
				for (String day: availableDaysIds) {
					String query = "SELECT * FROM " + AVAILABLEDAYS + " WHERE days=?;";
					 
					// the use of PreparedStatement prevents SQL injection 
					PreparedStatement st = conn.prepareStatement(query);
					st.setString(1, day);
					
					ResultSet r = st.executeQuery(); // ResultSet is a Cursor
					if (r.next()) {
						String dayy = r.getString("days");
						Date days = new Date();
						days.parse(dayy);
						
						availableDays.add(days);
					}
				}
				*/
    			
				ResultSet calendarEntryRs = rs.getArray(4).getResultSet();
				List<Date> keys = new ArrayList<>();
				while(calendarEntryRs.next()) 
					keys.add(new Date(rs.getDate(1).getTime()));
				
				Map<Date, String> calendarEntry = new HashMap<>();
				
				// get calendar entries from calendar 
				for (Date key: keys) {					
					String query = "SELECT * FROM " + CALENDAR_ENTRY + " WHERE day=?;";
					
					PreparedStatement st;
				
					st = conn.prepareStatement(query);
					st.setDate(1, new java.sql.Date(key.getTime()));
					
					ResultSet r = st.executeQuery(); // ResultSet is a Cursor
					
					if (r.next()) {
						String reservation = r.getString("reservation");														
						calendarEntry.put(key, reservation);
					}		
					r.close();
					st.close();
				}
    			
                	
				rs.close();
				stmt.close();
				conn.close();
				
                Calendar calendar = new Calendar();
                calendar.setId(rid.toString());
                calendar.setName(name);
                calendar.setEntityId(entityId);
                calendar.setAvailableDays(availableDays);
                calendar.setCalendarEntry(calendarEntry);
				return calendar;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private Collection<Calendar> getCalendars() {
        try {
 			List<Calendar> calendars = new ArrayList<Calendar>();
 			
 			Connection conn = JDBCConnection.getConnection();  			
 			String sql = "SELECT * FROM " + CALENDARS; 			 
 			// the use of PreparedStatement prevents SQL injection 
 			PreparedStatement stmt = conn.prepareStatement(sql); 			
 			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
 			
 			while (rs.next()) {	
 				String rid = rs.getString("id"); 
 				String name = rs.getString("name");
                String entityId = rs.getString("entityId");
                 
                 // get available days list 
                ResultSet ravailableDays = rs.getArray(3).getResultSet();
 				List<Date> availableDays = new ArrayList<Date>();
 				while(ravailableDays.next()) 
 					availableDays.add(ravailableDays.getDate(1));
 				     			
 				ResultSet calendarEntryRs = rs.getArray(4).getResultSet();
 				List<Date> keys = new ArrayList<>();
 				while(calendarEntryRs.next()) 
 					keys.add(new Date(rs.getDate(1).getTime()));
 				
 				Map<Date, String> calendarEntry = new HashMap<>();
 				
 				// get calendar entries from calendar 
 				for (Date key: keys) {					
 					String query = "SELECT * FROM " + CALENDAR_ENTRY + " WHERE day=?;";
 					
 					PreparedStatement st;
 				
 					st = conn.prepareStatement(query);
 					st.setDate(1, new java.sql.Date(key.getTime()));
 					
 					ResultSet r = st.executeQuery(); // ResultSet is a Cursor
 					
 					if (r.next()) {
 						String reservation = r.getString("reservation");														
 						calendarEntry.put(key, reservation);
 					}		
 					r.close();
 					st.close();
 				}
     			
                 	
 				rs.close();
 				stmt.close();
 				conn.close();
 				
	            Calendar calendar = new Calendar();
	            calendar.setId(rid.toString());
	            calendar.setName(name);
	            calendar.setEntityId(entityId);
	            calendar.setAvailableDays(availableDays);
	            calendar.setCalendarEntry(calendarEntry);
 				calendars.add(calendar);
             }
             return calendars;
         } catch (Exception e) {
             return null;
         }
	}    
}
