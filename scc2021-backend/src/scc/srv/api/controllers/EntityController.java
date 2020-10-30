package scc.srv.api.controllers;

import scc.data.Entity;
import scc.data.Reservation;
import scc.srv.api.EntityAPI;
import scc.srv.api.services.EntityService;

public class EntityController implements EntityAPI {

	private EntityService entities;

	public EntityController() {
		this.entities = new EntityService();
	}

	@Override
	public Entity get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity create(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity update(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCalendar(String id, String calendarId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMedia(String id, String mediaId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createReservation(String id, Reservation reservation) {
		// TODO Auto-generated method stub
		
	}
}
