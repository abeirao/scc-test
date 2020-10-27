package scc.srv.api.services;

import java.util.HashMap;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.Reservation;
import scc.srv.api.ReservationResource;

public class ReservationService implements ReservationResource {

	private Map<String, Reservation> reservations;
	private CosmosDBLayer cosmosDB;
	
	public ReservationService() {
		reservations = new HashMap<>();
		cosmosDB =  CosmosDBLayer.getInstance();
	}
	
	@Override
	public Reservation[] getReservationsFromEntity(String entityId) {
		return null;
	}
	
	@Override
	public Reservation[] getReservations() {
		return null;	
	}
	
	@Override
	public Reservation addReservation(Reservation reservation) {
		reservations.put(reservation.getId(), reservation);
		cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
		return reservation;
	}

	@Override
	public Reservation getReservation(String id) {
		// TODO Auto-generated method stub
		return cosmosDB.getReservation(id);
	}

	@Override
	public Reservation deleteReservation(String id) {
		return (Reservation) cosmosDB.delete(CosmosDBLayer.RESERVATIONS, id).getItem();
	}
}
