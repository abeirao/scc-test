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
	
	private Jedis jedis;
	private EntityService entityService;
	public PageService() {
		jedis = RedisCache.getCachePool().getResource();
		entityService = new EntityService();
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
			return entityService.getAll();
	}


	public boolean listedEntity(String entityId) {
		return entityService.get(entityId).isListed();

	}

	public Entity listEntity(String entityId) {
		Entity entity = entityService.get(entityId);

		if(entity.isListed() == true){
			entity.setListed(false);
		} else {
			entity.setListed(true);
		}
		return entityService.update(entity);
	}
}
