package scc.srv.api.controllers;

import java.util.Iterator;

import scc.data.Reservation;
import scc.srv.api.ReservationAPI;
import scc.srv.api.services.ReservationService;

public class ReservationController implements ReservationAPI {

	private ReservationService reservations;
	
	public ReservationController() {
		this.reservations = new ReservationService();
	}

	@Override
	public Reservation addReservation(Reservation reservation) {
		return reservations.addReservation(reservation);
	}
	
	@Override
	public Reservation updateReservation(Reservation reservation) {
		return reservations.update(reservation);
	}

	@Override
	public Reservation getReservation(String id) {
		return reservations.getReservation(id);
	}

	@Override
	public Reservation deleteReservation(String id) {
		return reservations.deleteReservation(id);
	}

	@Override
	public Iterator<Reservation> getReservations() {
		return reservations.getReservations();
	}

	@Override
	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		return reservations.getReservationsFromEntity(entityId);
	}
}
