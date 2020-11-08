package scc.srv.api.controllers;

import scc.data.Entity;
import scc.data.Reservation;
import scc.srv.api.EntityAPI;
import scc.srv.api.services.EntityService;
import java.util.Iterator;

public class EntityController implements EntityAPI {

	private EntityService entities;

	public EntityController() {
		this.entities = new EntityService();
	}

	@Override
	public Iterator<Entity> getAll() { 
		return entities.getAll();
	}
	
	@Override
	public Entity get(String id) {
		return entities.get(id);
	}

	@Override
	public Entity create(Entity entity) {
		return entities.create(entity);
	}

	@Override
	public Entity delete(String id) {
		return entities.delete(id);
	}

	@Override
	public Entity update(Entity entity) {
		return entities.update(entity);
	}

	@Override
	public void addCalendar(String id, String calendarId) {
		entities.addCalendar(id, calendarId);
	}

	@Override
	public void addMedia(String id, String mediaId) {
		entities.addMedia(id, mediaId);
	}

	@Override
	public void createReservation(String id, Reservation reservation) {
		entities.createReservation(id, reservation);
	}
}
