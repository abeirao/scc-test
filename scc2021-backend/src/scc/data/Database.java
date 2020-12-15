package scc.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import scc.data.DAO.*;

import java.util.logging.Level;


public class Database {    
	
	// Data Access Objects to manipulate data to and from the database
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
		Optional<Entity> entity = entities.get(Long.parseLong(id));
		return entity.isEmpty() ? null: entity.get();
	}

	public Iterator<Entity> getEntities() {
		return entities.getAll().iterator();
	}
// forums
	public void delForum(Forum forum) {
		forums.delete(forum);
	}

	public void putForum(Forum forum) {
		forums.save(forum);
	}

	public Forum getForumById(String id) {
		Optional<Forum> forum = forums.get(Long.parseLong(id));
		return forum.isEmpty() ? null : forum.get();
	}

	public Iterator<Forum> getForums() {
		return forums.getAll().iterator();
	}

// reservations
	public void delReservation(Reservation reservation) {
		reservations.delete(reservation);
	}

	public void putReservation(Reservation reservation) {
		reservations.save(reservation);
	}

	public Reservation getReservationById(String id) {
		Optional<Reservation> reservation = reservations.get(Long.parseLong(id));
		return reservation.isEmpty() ? null : reservation.get();
	}

	public Iterator<Reservation> getReservations() {
		return reservations.getAll().iterator();
	}

// calendars
	public void delCalendar(Calendar calendar) {
		calendars.delete(calendar);
	}

	public void putCalendar(Calendar calendar) {
		calendars.save(calendar);
	}

	public Calendar getCalendarById(String id) {
		Optional<Calendar> calendar = calendars.get(Long.parseLong(id));
		return calendar.isEmpty() ? null : calendar.get();
	}

	public Iterator<Calendar> getCalendars() {
		return calendars.getAll().iterator();
	}

	public void updateCalendar(Calendar calendar) {
		calendars.update(calendar);		
	}
	public void updateEntity(Entity entity) {
		entities.update(entity);		
	}
	public void updateForum(Forum forum) {
		forums.update(forum);		
	}
	public void updateReservation(Reservation reservation) {
		reservations.update(reservation);		
	}

	public Collection<Forum> getForumByEntity(String entityId) {		
		return ((ForumDAO) forums).getForumByEntity(entityId);
	}

	public Collection<Reservation> getReservationsByEntity(String entityId) {		
		return ((ReservationDAO) reservations).getReservationsByEntity(entityId);
	}
}
	
	


