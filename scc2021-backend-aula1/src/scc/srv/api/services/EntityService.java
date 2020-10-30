package scc.srv.api.services;

import java.util.HashMap;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.srv.api.EntityResource;

public class EntityService implements EntityResource {
	
	private CosmosDBLayer cosmosDB;
	
	public EntityService() {
		cosmosDB =  CosmosDBLayer.getInstance();
	}
	
	@Override
	public Entity get(String id) {
		return cosmosDB.getEntity(id);
	}

	@Override
	public Entity create(Entity entity) {
		cosmosDB.put(CosmosDBLayer.ENTITIES, entity);
		return entity;
	}

	@Override
	public Entity delete(String id) {
		return (Entity) cosmosDB.delete(CosmosDBLayer.ENTITIES, id).getItem();
	}

	@Override
	public Entity update(Entity entity) {
		cosmosDB.put(CosmosDBLayer.ENTITIES, entity);
		return entity;
	}
	
	@Override
	public void addMedia(String id, String mediaId) {
		Entity entity = cosmosDB.getEntity(id);
		String[] mediaIds = entity.getMediaIds();
		mediaIds = new String[mediaIds.length+1];
		mediaIds[mediaIds.length-1] = mediaId;		
	}
	
	@Override
	public void addCalendar(String id, String calendarId) {
		Entity entity = cosmosDB.getEntity(id);
		String[] calendarIds = entity.getMediaIds();
		calendarIds = new String[calendarIds.length+1];
		calendarIds[calendarIds.length-1] = calendarId;		
	}
	

	@Override
	public void createReservation(String id, Reservation reservation) {
		Entity entity = cosmosDB.getEntity(id);
		 
		// TODO
	}
	
}
