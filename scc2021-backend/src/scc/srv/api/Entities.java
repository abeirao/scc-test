package scc.srv.api;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import scc.data.Entity;
import scc.data.Reservation;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Forum;
import scc.data.Reservation;
import scc.exceptions.DayAlreadyOccupiedException;
import scc.redis.RedisCache;
import scc.srv.api.services.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* MOCK ENTITY API, TESTING ONLY */

@Path("/entities")
public class Entities {

	static final String ENDPOINT = "/entity";

	public static final String ENTITY_KEY_PREFIX = "entity: ";

	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	private CalendarService calendarService;
	private ReservationService reservationService;
	private ForumService forums;
	
	public Entities() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
		calendarService = new CalendarService();
		reservationService = new ReservationService();
		forums = new ForumService();
	}
	/**
	 *
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getString(String id) { // testing
		/*Entity entity;
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
		} 	*/
		return "get entity"; 
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
	

	/**
	 *
	 * @param entity
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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

	/**
	 *
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Entity delete(String id) {
		Entity entity = this.get(id);
		jedis.del(ENTITY_KEY_PREFIX + id);
		return (Entity) cosmosDB.delete(CosmosDBLayer.ENTITIES, entity).getItem();
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Entity update(Entity entity) {
		cosmosDB.update(CosmosDBLayer.ENTITIES, entity);
		try {
			jedis.set(ENTITY_KEY_PREFIX + entity.getId(), mapper.writeValueAsString(entity));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 *
	 * @param id
	 * @param mediaId
	 */
	@PUT
	@Path("/{id}/media/{mediaId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMedia(String id, String mediaId) {
		Entity entity = cosmosDB.getEntity(id);
		String[] mediaIds = entity.getMediaIds();
		mediaIds = new String[mediaIds.length+1];
		mediaIds[mediaIds.length-1] = mediaId;		
		
		this.update(entity); // update entity 
	}

	/**
	 *
	 * @param id
	 * @param reservation
	 */
	@POST
	@Path("/{id}/reservations")
	@Consumes(MediaType.APPLICATION_JSON)
	void createReservation(@PathParam("id") String id, Reservation reservation) {
		try {
			Entity entity = this.get(id);

			Calendar calendar = calendarService.get(entity.getCalendarId());
			List<Date> availableDays = calendar.getAvailableDays();
			Date day = null;
			if(availableDays.size() != 0) {
				day = availableDays.get(availableDays.indexOf(reservation.getDay()));
			}

			if(day != null)
				throw new DayAlreadyOccupiedException();
			else {
				calendar.putReservation(reservation, reservation.getDay());
				reservationService.addReservation(reservation);
				calendarService.update(calendar);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Iterator<Entity> getAll() {
		return cosmosDB.getAllEntities().iterator(); //return "you called get all";
	}
	
}
