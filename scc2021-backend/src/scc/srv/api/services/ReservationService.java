package scc.srv.api.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.azure.cosmos.implementation.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.CosmosDBLayer;
import scc.data.Reservation;
import scc.redis.RedisCache;

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
	
	public Reservation get(String id) throws NotFoundException {
		Reservation reservation = null;
		try {
			reservation = mapper.readValue(jedis.get(RESERVATION_KEY_PREFIX + id), Reservation.class);		
			if (reservation == null) {
				reservation = cosmosDB.getReservation(id);				
				jedis.set(RESERVATION_KEY_PREFIX + id, mapper.writeValueAsString(reservation));				
			}
			return reservation;	
		} catch (NotFoundException e) {
            throw e;		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Iterator<Reservation> getReservationsFromEntity(String entityId) throws NotFoundException {
		try {
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
	        // if query not on cache
			return cosmosDB.getReservationsByEntity(entityId).iterator();
		
		} catch (NotFoundException e) {
            throw e;		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Iterator<Reservation> getReservations() {		
		return cosmosDB.getAllReservations().iterator();	
	}
	
	public Reservation addReservation(Reservation reservation) {
    	reservation.setId(Utils.randomUUID().toString());
    	
		cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);		
		try {
			// add reservation to cache
			jedis.set(RESERVATION_KEY_PREFIX + reservation.getId(), mapper.writeValueAsString(reservation));	
			// add reservation to entity reservations in cache
			jedis.sadd(RESERVATION_ENTITY_KEY_PREFIX + reservation.getEntityId(), mapper.writeValueAsString(reservation)); 
		
			return reservation;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public Reservation update(Reservation reservation) {
		try {
			jedis.set(RESERVATION_KEY_PREFIX + reservation.getId(), mapper.writeValueAsString(reservation));	
			// add reservation to entity reservations in cache
			jedis.sadd(RESERVATION_ENTITY_KEY_PREFIX + reservation.getEntityId(), mapper.writeValueAsString(reservation)); 
		
			cosmosDB.update(CosmosDBLayer.RESERVATIONS, reservation);
			return reservation;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Reservation delete(String id) throws NotFoundException {
		try {
			Reservation reservation = this.get(id);
			// delete reservation from cache
			jedis.del(RESERVATION_KEY_PREFIX + id);
			// delete reservation from reservations by entity on cache 
			jedis.srem(RESERVATION_ENTITY_KEY_PREFIX + reservation.getEntityId(), mapper.writeValueAsString(reservation));
			// delete reservation from database
			cosmosDB.delete(CosmosDBLayer.RESERVATIONS, reservation).getItem();
			return reservation;
		} catch (NotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
