package scc.data;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;

import javax.ws.rs.NotFoundException;

public class CosmosDBLayer {
	
	// TODO geo replication
	// https://docs.microsoft.com/en-gb/azure/cosmos-db/how-to-multi-master?tabs=api-async
	
	private static final String CONNECTION_URL = "https://scc-cosmos-50415.documents.azure.com:443/";
	private static final String DB_KEY = "bbgF4Re4UQMuEsX0MZSEjuLDCZdMU76srR5VLAnaluK5QuXKcZUeKeFPEp8mMAgZwTMcAUz6T8oW61WTIh5ymg==";//primary connection string> "AccountEndpoint=https://scc-cosmos-50415.documents.azure.com:443/;AccountKey=bbgF4Re4UQMuEsX0MZSEjuLDCZdMU76srR5VLAnaluK5QuXKcZUeKeFPEp8mMAgZwTMcAUz6T8oW61WTIh5ymg==;";
	private static final String DB_NAME = "scc50415p";
	//private static final String REGION = ""; Geo-Replication qual é a região ?

	enum Containers {
		ENTITIES, RESERVATIONS, CALENDARS, FORUMS
	}
	
	public static final String ENTITIES = "entities";
	public static final String CALENDARS = "calendars";
	public static final String RESERVATIONS = "reservations";
	public static final String FORUMS = "forums";
	
	private static CosmosDBLayer instance;

	public static synchronized CosmosDBLayer getInstance() {
		if( instance != null)
			return instance;


		//GEO REPLICATION CODE
		//ArrayList<String> preferredRegions = new ArrayList<String>();
		//preferredRegions.add(REGION);



		CosmosClient client = new CosmosClientBuilder()
		         .endpoint(CONNECTION_URL)
		         .key(DB_KEY)
		         .directMode()		// comment this is not to use direct mode
				 //.multipleWriteRegionsEnabled(true) Descomentar para testar geoReplication
				 //.preferredRegions(preferredRegions)
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
	private CosmosContainer forums;
	
	public CosmosDBLayer(CosmosClient client) {
		this.client = client;
	}
	
	private synchronized void init() {
		if( db != null)
			return;

		db = client.getDatabase(DB_NAME);
		createContainers();
		entities = db.getContainer(Containers.ENTITIES.toString().toLowerCase());
		reservations = db.getContainer(Containers.RESERVATIONS.toString().toLowerCase());
		calendars = db.getContainer(Containers.CALENDARS.toString().toLowerCase());
		forums = db.getContainer(Containers.FORUMS.toString().toLowerCase());
	}

	private void createContainers() {
		db.createContainerIfNotExists(new CosmosContainerProperties(Containers.ENTITIES.toString().toLowerCase(), "/id"));
		db.createContainerIfNotExists(new CosmosContainerProperties(Containers.RESERVATIONS.toString().toLowerCase(), "/id"));
		db.createContainerIfNotExists(new CosmosContainerProperties(Containers.CALENDARS.toString().toLowerCase(), "/id"));
		db.createContainerIfNotExists(new CosmosContainerProperties(Containers.FORUMS.toString().toLowerCase(), "/id"));
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
			case FORUMS:
				return forums.deleteItem(item, new CosmosItemRequestOptions());
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
			case FORUMS:
				return forums.createItem(item);
			default:
				return null;
		}
	}
	
	public CosmosItemResponse<Object> update(String container, Object item) {
		init();
		switch (container) {
			case RESERVATIONS:
				return reservations.upsertItem(item);
			case CALENDARS:
				return calendars.upsertItem(item);
			case ENTITIES: 
				return entities.upsertItem(item);
			case FORUMS:
				return forums.upsertItem(item);
			default:
				return null;
		}
	}

	public Reservation getReservation(String id) throws NotFoundException {
		CosmosItemResponse<Reservation> res = calendars.readItem(id, new PartitionKey(id), Reservation.class);
		if (res.getStatusCode() == 404)
			throw new NotFoundException();
		return res.getItem(); 
	}
	
	public Calendar getCalendar(String id) throws NotFoundException {
		CosmosItemResponse<Calendar> res = calendars.readItem(id, new PartitionKey(id), Calendar.class);
		if (res.getStatusCode() == 404)
			throw new NotFoundException();
		return res.getItem(); 
	}
	
	public Forum getForum(String id) throws NotFoundException {
		CosmosItemResponse<Forum> res = forums.readItem(id, new PartitionKey(id), Forum.class);
		if (res.getStatusCode() == 404)
			throw new NotFoundException();
		return res.getItem(); 
	}
	
	public Entity getEntity(String id) throws NotFoundException {
		CosmosItemResponse<Entity> res = entities.readItem(id, new PartitionKey(id), Entity.class);
		if (res.getStatusCode() == 404)
			throw new NotFoundException();
		return res.getItem();
	}
	

	public CosmosPagedIterable<Reservation> getAllReservations() {
		init();
		String query = "SELECT * FROM " + RESERVATIONS + " ";
		return reservations.queryItems(query, new CosmosQueryRequestOptions(), Reservation.class);
	}
	
	public CosmosPagedIterable<Calendar> getAllCalendars() {
		init();
		String query = "SELECT * FROM " + CALENDARS + " ";
		return calendars.queryItems(query, new CosmosQueryRequestOptions(), Calendar.class);
	}
	
	public CosmosPagedIterable<Forum> getAllForums() {
		init();
		String query = "SELECT * FROM " + FORUMS + " ";
		return forums.queryItems(query, new CosmosQueryRequestOptions(), Forum.class);
	}
	
	public CosmosPagedIterable<Entity> getAllEntities() {
		init();
		String query = "SELECT * FROM " + ENTITIES + " ";
		return entities.queryItems(query, new CosmosQueryRequestOptions(), Entity.class);
	}
	
	public CosmosPagedIterable<Forum> getForumByEntity(String entityId) {
		init();
		String query = "SELECT * FROM " + FORUMS + " WHERE entityId=\"" + entityId + "\"";
		return forums.queryItems(query, new CosmosQueryRequestOptions(), Forum.class);
	}
	
	public CosmosPagedIterable<Reservation> getReservationsByEntity(String entityId) {
		init();
		String query = "SELECT * FROM " + RESERVATIONS + " WHERE entityId=\"" + entityId + "\"";
		return reservations.queryItems(query, new CosmosQueryRequestOptions(), Reservation.class);
	}

	public CosmosDatabase getDatabase(String property) {
		// TODO Auto-generated method stub
		return this.db;
	}

	
}
