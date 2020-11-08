package scc.srv.api.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.PageAPI;

public class PageService {

	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;

	public PageService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}
	
	public Iterator<Entity> popularEntities() {
		// popular entities are the ones in the cache
		Set<String> keys = jedis.keys(EntityService.ENTITY_KEY_PREFIX + "*");
		if (keys.size() > 0) {
			Set<Entity> entities = new HashSet<Entity>();
			for (String key : keys) {
				try {
					entities.add(mapper.readValue(jedis.get(key), Entity.class));
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }  
			} 	
			return entities.iterator();
		}
		else  // if no entities are in cache, return all entities
			return new EntityService().getAll();		
	}

	
	public String listedEntity(String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setEntityLike(String entityId) {
		// TODO Auto-generated method stub
		
	}

	
	public String reservationStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

}
