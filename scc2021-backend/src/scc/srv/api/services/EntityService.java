package scc.srv.api.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;

import java.text.SimpleDateFormat;
import java.util.Date;
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
		Entity entity;
		try {
			entity = mapper.readValue(jedis.get(ENTITY_KEY_PREFIX + id), Entity.class);
	
			if (entity == null) {
				entity = cosmosDB.getEntity(id);
				jedis.set(ENTITY_KEY_PREFIX + id, mapper.writeValueAsString(entity));				
			}
			return entity;
		} catch (Exception e) {
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


	public void createReservation(String id, Reservation reservation) {
		try {
			Entity entity = this.get(id);
			CalendarService cs = new CalendarService();
			ReservationService rs = new ReservationService();
			Calendar calendar = cs.get(entity.getCalendarId());
			calendar.putReservation(id, reservation, new SimpleDateFormat("dd/MM/yyyy").parse(reservation.getDay()));
			cs.update(calendar);
			rs.addReservation(reservation);
			
			this.update(entity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
