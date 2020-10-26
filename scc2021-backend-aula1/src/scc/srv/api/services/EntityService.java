package scc.srv.api.services;

import java.util.HashMap;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Database;
import scc.data.Entity;
import scc.srv.api.EntityResource;

public class EntityService implements EntityResource {
	
	// TODO
	private Map<String, Entity> entities;
	private CosmosDBLayer cosmosDB;
	
	public EntityService() {
		entities = new HashMap<>();
		cosmosDB =  CosmosDBLayer.getInstance();
	}
	
	@Override
	public Entity get(String id) {
		return null;
	}

	@Override
	public String create(Entity entity) {
		return null;
	}

	@Override
	public Entity delete(String id) {
		return (Entity) cosmosDB.delete(CosmosDBLayer.ENTITIES, id).getItem();
	}

	@Override
	public Entity update(Entity entity) {
		return null;
	}

}
