package scc.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Database {    

	
	String entities = "CREATE TABLE IF NOT EXISTS entities (id serial PRIMARY KEY, name VARCHAR(25))";
	
    int id = 6;
    String entity = "Trygve Gulbranssen";
    String query = "INSERT INTO entities(id, name) VALUES(?, ?)";
    
    
    public void putEntity(String entity) {

		try (Connection con = JDBCConnection.getConnection();
	         PreparedStatement pst = con.prepareStatement(query)) {
	        
	        pst.setInt(1, id);
	        pst.setString(2, entity);
	        pst.executeUpdate();
	
	    } catch (SQLException ex) {
	
	        Logger lgr = Logger.getLogger(Database.class.getName());
	        lgr.log(Level.SEVERE, ex.getMessage(), ex);
	    }
    }
}
	
	


