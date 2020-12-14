package scc.data.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import scc.data.JDBCConnection;
import scc.data.Reservation;

public class ReservationDAO implements DAO<Reservation, Long> {

    private static final String RESERVATIONS = "reservations";

    public ReservationDAO() {
        // create table
        create();
    }

    private void create() {        
        String query = "CREATE TABLE IF NOT EXISTS " + RESERVATIONS +
                        " (id TEXT)," +
                        " (name TEXT)," + 
                        " (entityId TEXT),"+ 
                        " (day DATE)";

        try (Connection con = JDBCConnection.getConnection()) {
            //PreparedStatement pst = con.prepareStatement(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }


    public Optional<Reservation> get(Long id) {
        Reservation reservation = findReservation(id);
        if (reservation == null)
            return Optional.empty();
        return Optional.of(reservation);
    }

    public Collection<Reservation> getAll() {
        List<Reservation> reservations = getReservations();
        return reservations;
    }

	public Optional<Long> save(Reservation reservation) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "INSERT INTO " + RESERVATIONS +
                    	" (id, name, entityId, day) VALUES (?, ?, ?, ?)";
			
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, reservation.getId().toString());
			stmt.setString(2, reservation.getName());
			stmt.setString(3, reservation.getEntityId());
			stmt.setDate(4, (java.sql.Date) reservation.getDay());			
			
			int rows = stmt.executeUpdate();
			return Optional.of(Long.parseLong(reservation.getId()));
        } catch (Exception e) {
        	
        }
    }

    public void update(Reservation reservation) {
    	
    }

    public void delete(Reservation reservation) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "DELETE * FROM " + RESERVATIONS + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, reservation.getId().toString());
			
			int rows = stmt.executeUpdate();
        } catch (Exception e) {
        	
        }
    }
    

    private Reservation findReservation(Long id) {
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + RESERVATIONS + " WHERE id=?;";
			 
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id.toString());
			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			if (rs.next()) {	// if the user exists
				String rid = rs.getString("id"); // get by column label
				String name = rs.getString("name"); 
                String entityId = rs.getString("entityId"); 
                Date day = rs.getDate("day");
							
				rs.close();
				stmt.close();
				conn.close();
				
                Reservation reservation = new Reservation();
                reservation.setId(rid.toString());
                reservation.setName(name);
                reservation.setEntityId(entityId);
                reservation.setDay(day);
				return reservation;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private List<Reservation> getReservations() {
    	List<Reservation> reservations = new LinkedList<Reservation>();
        try {
			Connection conn = JDBCConnection.getConnection(); 
			
			String sql = "SELECT * FROM " + RESERVATIONS + "";
			// the use of PreparedStatement prevents SQL injection 
			PreparedStatement stmt = conn.prepareStatement(sql);			
			ResultSet rs = stmt.executeQuery(); // ResultSet is a Cursor
			
			while (rs.next()) {	// if the user exists
				String rid = rs.getString("id"); // get by column label
				String name = rs.getString("name"); 
                String entityId = rs.getString("entityId"); 
                Date day = rs.getDate("day");
											
                Reservation reservation = new Reservation();
                reservation.setId(rid.toString());
                reservation.setName(name);
                reservation.setEntityId(entityId);
                reservation.setDay(day);
                
				reservations.add(reservation);
            }
			rs.close();
			stmt.close();
			conn.close();
	    	return reservations;
        } catch (Exception e) {
            return null;
        }
	}

}
