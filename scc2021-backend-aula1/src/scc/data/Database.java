package scc.data;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.Entity;
import scc.data.CosmosDBLayer;

public class Database {
	CosmosDBLayer db;
	
	public Database() {
		db = CosmosDBLayer.getInstance();
	}
	
	// TODO ????????
	
	public Entity get(String id) {
		CosmosPagedIterable<Entity> resGet = db.getEntityById(id);	
		return resGet.iterator().next(); 
	}
	
	public Entity put(Entity entity) {
		CosmosItemResponse<Entity> res = db.putEntity(entity);
		return res.getItem();
	}

	public Entity delete(String id) {
		CosmosPagedIterable<Entity> resGet = db.getEntityById(id);	
		return (Entity) db.delEntity(resGet.iterator().next()).getItem();
	}	
}
