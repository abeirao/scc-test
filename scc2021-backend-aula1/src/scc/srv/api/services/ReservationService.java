package scc.srv.api.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.Reservation;
import scc.srv.api.ReservationResource;

public class ReservationService implements ReservationResource {

	private CosmosDBLayer cosmosDB;
	
	public ReservationService() {
		cosmosDB =  CosmosDBLayer.getInstance();
	}
	
	// TODO add caching
	
	@Override
	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		return cosmosDB.getReservationsByEntity(entityId).iterator();
	}
	
	@Override
	public Iterator<Reservation> getReservations() {		
		return cosmosDB.getAllReservations().iterator();	
	}
	
	@Override
	public Reservation addReservation(Reservation reservation) {
		cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
		return reservation;
	}

	@Override
	public Reservation getReservation(String id) {
		return cosmosDB.getReservation(id);
	}

	@Override
	public Reservation deleteReservation(String id) {
		Reservation reservation = this.getReservation(id);
		return (Reservation) cosmosDB.delete(CosmosDBLayer.RESERVATIONS, reservation).getItem();
	}
}
