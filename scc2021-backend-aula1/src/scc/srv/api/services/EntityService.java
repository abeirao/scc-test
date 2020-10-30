package scc.srv.api.services;

import java.util.HashMap;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Entity;
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

	public void createReservation() {
		
	}
}
