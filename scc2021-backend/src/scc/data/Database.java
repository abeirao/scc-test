package scc.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Logger;

import scc.data.DAO.*;

import java.util.logging.Level;


public class Database {    

	private static final String ENTITIES = "entities";	
	private DAO<Forum, Long> forums;
	private DAO<Reservation, Long> reservations;
	private DAO<Entity, Long> entities;
	private DAO<Calendar, Long> calendars;

	public Database() {
		forums = new ForumDAO();
		entities = new EntityDAO();
		reservations = new ReservationDAO();
		calendars = new CalendarDAO();
	}

	public void init(){

	}
    
	
	public void delEntity(Entity entity) {
		entities.delete(entity);
	}
	
	public void putEntity( Entity entity) {
		entities.save(entity);
	}
	
	public Entity getEntityById( String id) {
		Optional<Entity> entity = entities.get(id);
		
	}

	public Iterator<Entity> getEntities() {
		return entities.getAll().iterator();
	}
}
	
	


