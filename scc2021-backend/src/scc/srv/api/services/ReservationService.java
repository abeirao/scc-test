package scc.srv.api.services;

import java.util.HashMap;
import java.util.HashSet;
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

	public static final String RESERVATION_KEY_PREFIX = "reservation: ";
	public static final String RESERVATION_ENTITY_KEY_PREFIX = "entityReservations: ";
	
	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	
	public ReservationService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}

	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		Set<Reservation> reservations = new HashSet<Reservation>();
        Set<String> s = jedis.smembers(RESERVATION_ENTITY_KEY_PREFIX + entityId);
        if (s != null) {
	        s.forEach(x -> {
	            try {
	                reservations.add(mapper.readValue(x, Reservation.class));
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        });
	        return reservations.iterator();
        }				
		return cosmosDB.getReservationsByEntity(entityId).iterator();
		
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
		
			cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
			return reservation;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
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
