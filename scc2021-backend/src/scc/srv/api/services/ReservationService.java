package scc.srv.api.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.ReservationAPI;

public class ReservationService {

	protected final String RESERVATION_KEY_PREFIX = "reservation: ";
	protected final String RESERVATION_ENTITY_KEY_PREFIX = "entityReservations: ";
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	
	public ReservationService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}

	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		String reservations = jedis.get(RESERVATION_ENTITY_KEY_PREFIX + entityId); 
		if (reservations != null)
			return null; // TODO reservations to Set
		return cosmosDB.getReservationsByEntity(entityId).iterator();
	}
	
	public Iterator<Reservation> getReservations() {		
		return cosmosDB.getAllReservations().iterator();	
	}
	
	public Reservation addReservation(Reservation reservation) {
		// add reservation to cache
		jedis.set(RESERVATION_KEY_PREFIX + reservation.getId(), reservation.toString());
		// add reservation to entity reservations in cache
		jedis.sadd(RESERVATION_ENTITY_KEY_PREFIX + reservation.getEntityId(), reservation.toString()); 
		cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
		return reservation;
	}
	
	public Reservation getReservation(String id) {
		Reservation reservation = Reservation.fromString(jedis.get(RESERVATION_KEY_PREFIX + id));
		if (reservation == null)
			reservation = cosmosDB.getReservation(id);
		
		return reservation;
	}

	public Reservation deleteReservation(String id) {
		Reservation reservation = this.getReservation(id);
		jedis.del(RESERVATION_KEY_PREFIX + id);
		return (Reservation) cosmosDB.delete(CosmosDBLayer.RESERVATIONS, reservation).getItem();
	}
}
