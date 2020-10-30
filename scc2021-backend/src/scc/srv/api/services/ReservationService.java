package scc.srv.api.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.Reservation;
import scc.srv.api.ReservationAPI;

public class ReservationService {

	private CosmosDBLayer cosmosDB;
	private HashMap<String, Reservation> reservations;
	
	public ReservationService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		reservations = new HashMap<>();	// used for faster access
	}

	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		return cosmosDB.getReservationsByEntity(entityId).iterator();
	}
	
	public Iterator<Reservation> getReservations() {		
		return cosmosDB.getAllReservations().iterator();	
	}
	
	public Reservation addReservation(Reservation reservation) {
		reservations.put(reservation.getId(), reservation);
		cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
		return reservation;
	}
	
	public Reservation getReservation(String id) {
		Reservation reservation = reservations.get(id);
		if (reservation == null)
			reservation = cosmosDB.getReservation(id);
		
		return reservation;
	}

	public Reservation deleteReservation(String id) {
		Reservation reservation = this.getReservation(id);
		if (reservations.containsKey(id)) reservations.remove(id);
		return (Reservation) cosmosDB.delete(CosmosDBLayer.RESERVATIONS, reservation).getItem();
	}
}
