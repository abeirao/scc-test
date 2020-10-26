package scc.data;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;

public class CosmosDBLayer {
	private static final String CONNECTION_URL = "https://scc-50415.documents.azure.com:443/";
	private static final String DB_KEY = "5R65BmoJPwE2t1DMlwItCyz6zXJIob69DIwDi0dQ3LwIp0mI9EZKMl5jqSjL2lBtc84NZk5EvM6iAGztayN9Qg==";
	private static final String DB_NAME = "scc50415";
	
	enum Containers {
		ENTITIES, RESERVATIONS, CALENDARS
	}
	
	public static final String ENTITIES = "entities";
	public static final String CALENDARS = "calendars";
	public static final String RESERVATIONS = "reservations";
	
	private static CosmosDBLayer instance;

	public static synchronized CosmosDBLayer getInstance() {
		if( instance != null)
			return instance;

		CosmosClient client = new CosmosClientBuilder()
		         .endpoint(CONNECTION_URL)
		         .key(DB_KEY)
		         .directMode()		// comment this is not to use direct mode
		         .consistencyLevel(ConsistencyLevel.SESSION)
		         .connectionSharingAcrossClientsEnabled(true)
		         .contentResponseOnWriteEnabled(true)
		         .buildClient();
		instance = new CosmosDBLayer( client);
		return instance;
		
	}
	
	private CosmosClient client;
	private CosmosDatabase db;
	
	private CosmosContainer entities;
	private CosmosContainer reservations;
	private CosmosContainer calendars;
	
	public CosmosDBLayer(CosmosClient client) {
		this.client = client;
	}
	
	private synchronized void init() {
		if( db != null)
			return;
		db = client.getDatabase(DB_NAME);
		entities = db.getContainer(Containers.ENTITIES.toString().toLowerCase());
		reservations = db.getContainer(Containers.RESERVATIONS.toString().toLowerCase());
		calendars = db.getContainer(Containers.CALENDARS.toString().toLowerCase());
		
	}

	public CosmosItemResponse<Object> delEntity(Entity entity) {
		init();
		return entities.deleteItem(entity, new CosmosItemRequestOptions());
	}
	
	public CosmosItemResponse<Entity> putEntity( Entity entity) {
		init();
		return entities.createItem(entity);
	}
	
	public CosmosPagedIterable<Entity> getEntityById( String id) {
		init();
		return entities.queryItems("SELECT * FROM entities WHERE entities.id=\"" + id + "\"", new CosmosQueryRequestOptions(), Entity.class);
	}

	public CosmosPagedIterable<Entity> getEntities() {
		init();
		return entities.queryItems("SELECT * FROM entities ", new CosmosQueryRequestOptions(), Entity.class);
	}

	public void close() {
		client.close();
	}
	
	
	public CosmosItemResponse<Object> delete(String container, Object item) {
		init();
		switch (container) {
			case RESERVATIONS:
				return reservations.deleteItem(item, new CosmosItemRequestOptions());
			case CALENDARS:
				return calendars.deleteItem(item, new CosmosItemRequestOptions());
			case ENTITIES:
				return entities.deleteItem(item, new CosmosItemRequestOptions());
			default:
				return null;
		}		
	}
	
	public CosmosItemResponse<Object> put(String container, Object item) {
		init();
		switch (container) {
			case RESERVATIONS:
				return reservations.createItem(item);
			case CALENDARS:
				return calendars.createItem(item);
			case ENTITIES:
				return entities.createItem(item);
			default:
				return null;
		}
	}
	
	public CosmosPagedIterable<Object> getById(String container, String id) {
		init();
		String query = "SELECT * FROM " + container + " " + container + ".id=\"" + id + "\"";

		switch (container) {
			case RESERVATIONS:
				return reservations.queryItems(query, new CosmosQueryRequestOptions(), Reservation.class);
			case CALENDARS:
				return calendars.queryItems(query, new CosmosQueryRequestOptions(), Calendar.class);
			case ENTITIES:
				return entities.queryItems(query, new CosmosQueryRequestOptions(), Entity.class);
			default:
				return null;
		}
	}
	
	public CosmosPagedIterable<Object> getAll(String container) {
		init();
		String query = "SELECT * FROM " + container + " ";

		switch (container) {
			case RESERVATIONS:
				return reservations.queryItems(query, new CosmosQueryRequestOptions(), Reservation.class);
			case CALENDARS:
				return calendars.queryItems(query, new CosmosQueryRequestOptions(), Calendar.class);
			case ENTITIES:
				return entities.queryItems(query, new CosmosQueryRequestOptions(), Entity.class);
			default:
				return null;
		}
	}
	
	
}
