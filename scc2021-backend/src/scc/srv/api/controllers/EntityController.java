package scc.srv.api.controllers;

import scc.data.Calendar;
import scc.data.Entity;
import scc.data.Reservation;
import scc.exceptions.DayAlreadyOccupiedException;
import scc.srv.api.EntityAPI;
import scc.srv.api.services.EntityService;

import java.util.Date;
import java.util.Iterator;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path(EntityAPI.ENDPOINT)
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
		try {
			return entities.get(id);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@Override
	public Entity create(Entity entity) {
		return entities.create(entity);
	}

	@Override
	public Entity delete(String id) {
		try {
			return entities.delete(id);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Override
	public Entity update(Entity entity) {
		return entities.update(entity);
	}

	@Override
	public void addMedia(String id, String mediaId) {
		entities.addMedia(id, mediaId);
	}

	@Override
	public void createReservation(Reservation reservation) {
		try {
			entities.createReservation(reservation);
		} catch (DayAlreadyOccupiedException e) {
			System.out.println("Day already occupied with a reservation");
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
	}

	@Override
	public Calendar createCalendar(Calendar calendar) {
		try {
			return entities.createCalendar(calendar);
		} catch (NotFoundException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
	}
}
