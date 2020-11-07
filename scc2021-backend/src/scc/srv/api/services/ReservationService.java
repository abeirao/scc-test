package scc.srv.api.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.azure.core.util.serializer.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.Reservation;
import scc.data.ReservationSet;
import scc.redis.RedisCache;
import scc.srv.api.ReservationAPI;

public class ReservationService {

	protected final String RESERVATION_KEY_PREFIX = "reservation: ";
	protected final String RESERVATION_ENTITY_KEY_PREFIX = "entityReservations: ";
	
	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	
	public ReservationService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}

	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		ReservationSet reservations = null;
		try {
			reservations = mapper.readValue(jedis.get(RESERVATION_ENTITY_KEY_PREFIX + entityId), ReservationSet.class);
								
			if (reservations != null)
				return reservations.getReservations().iterator(); 
			
			return cosmosDB.getReservationsByEntity(entityId).iterator();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Iterator<Reservation> getReservations() {		
		return cosmosDB.getAllReservations().iterator();	
	}
	
	public Reservation addReservation(Reservation reservation) {
		// add reservation to cache
		try {
			jedis.set(RESERVATION_KEY_PREFIX + reservation.getId(), mapper.writeValueAsString(reservation));	
			// add reservation to entity reservations in cache
			jedis.sadd(RESERVATION_ENTITY_KEY_PREFIX + reservation.getEntityId(), mapper.writeValueAsString(reservation)); 
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
		return reservation;
		
	}
	
	public Reservation getReservation(String id) {
		Reservation reservation = null;
		try {
			reservation = mapper.readValue(jedis.get(RESERVATION_KEY_PREFIX + id), Reservation.class);		
			if (reservation == null) {
				reservation = cosmosDB.getReservation(id);				
				jedis.set(RESERVATION_KEY_PREFIX + id, mapper.writeValueAsString(reservation));				
			}
			return reservation;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Reservation deleteReservation(String id) {
		Reservation reservation = this.getReservation(id);
		jedis.del(RESERVATION_KEY_PREFIX + id);
		return (Reservation) cosmosDB.delete(CosmosDBLayer.RESERVATIONS, reservation).getItem();
	}
}
