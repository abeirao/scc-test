package scc.srv.api.services;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.EntityAPI;

public class EntityService   {
	
	private final String ENTITY_KEY_PREFIX = "entity: ";

	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	
	public EntityService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}
	
	public Entity get(String id) { 
		String entity = jedis.get(ENTITY_KEY_PREFIX + id);
		if (entity != null)
			return Entity.getEntityFromString(entity);
		return cosmosDB.getEntity(id);
	}

	public Entity create(Entity entity) {
		cosmosDB.put(CosmosDBLayer.ENTITIES, entity); // add to db
		jedis.set(ENTITY_KEY_PREFIX + entity.getId(), entity.toString()); // add to cache
		return entity;
	}

	public Entity delete(String id) {
		Entity entity = this.get(id);
		jedis.del(ENTITY_KEY_PREFIX + id);
		return (Entity) cosmosDB.delete(CosmosDBLayer.ENTITIES, entity).getItem();
	}

	public Entity update(Entity entity) {
		cosmosDB.put(CosmosDBLayer.ENTITIES, entity);
		jedis.set(ENTITY_KEY_PREFIX + entity.getId(), entity.toString());
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
