package scc.srv.api.services;

import scc.data.Database;
import scc.data.Entity;
import scc.srv.api.EntityResource;

public class EntityService implements EntityResource {
	
	// TODO
	
	private Database db;
	
	public EntityService() {
		db = new Database();
	}
	
	@Override
	public Entity get(String id) {
		return db.get(id);
	}

	@Override
	public String create(Entity entity) {
		return db.put(entity).getId();
	}

	@Override
	public Entity delete(String id) {
		return db.delete(id);
	}

	@Override
	public Entity update(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
