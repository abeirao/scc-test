package scc.srv.api.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;

import java.util.Iterator;

public class EntityService   {
	
	public static final String ENTITY_KEY_PREFIX = "entity: ";

	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	
	public EntityService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}
	
	public Iterator<Entity> getAll() {
		return cosmosDB.getAllEntities().iterator(); 		
	}
	
	public Entity get(String id) { 
		Entity entity = null;
		try {
			entity = mapper.readValue(jedis.get(ENTITY_KEY_PREFIX + id), Entity.class);
	
			if (entity == null) {
				entity = cosmosDB.getEntity(id);
				jedis.set(ENTITY_KEY_PREFIX + id, mapper.writeValueAsString(entity));				
			}
			return entity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 		
	}

	public Entity create(Entity entity) {
		try {
			 // add to db
			cosmosDB.put(CosmosDBLayer.ENTITIES, entity);
			// add to cache
			jedis.set(ENTITY_KEY_PREFIX + entity.getId(), mapper.writeValueAsString(entity)); 
			return entity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Entity delete(String id) {
		Entity entity = this.get(id);
		jedis.del(ENTITY_KEY_PREFIX + id);
		return (Entity) cosmosDB.delete(CosmosDBLayer.ENTITIES, entity).getItem();
	}

	public Entity update(Entity entity) {
		cosmosDB.put(CosmosDBLayer.ENTITIES, entity);
		try {
			jedis.set(ENTITY_KEY_PREFIX + entity.getId(), mapper.writeValueAsString(entity));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}
	
	public void addMedia(String id, String mediaId) {
		Entity entity = cosmosDB.getEntity(id);
		String[] mediaIds = entity.getMediaIds();
		mediaIds = new String[mediaIds.length+1];
		mediaIds[mediaIds.length-1] = mediaId;		
		
		this.update(entity); // update entity 
	}
	
	public void addCalendar(String id, String calendarId) {
		Entity entity = cosmosDB.getEntity(id);
		String[] calendarIds = entity.getMediaIds();
		calendarIds = new String[calendarIds.length+1];
		calendarIds[calendarIds.length-1] = calendarId;	
		
		this.update(entity);
	}
	

	public void createReservation(String id, Reservation reservation) {
		Entity entity = cosmosDB.getEntity(id);
		 
		// TODO - create reservation for this entity
		// should call other service's methods ???
	}
	
}
