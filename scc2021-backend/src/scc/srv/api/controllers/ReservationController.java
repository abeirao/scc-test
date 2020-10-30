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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reservation getReservation(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reservation deleteReservation(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Reservation> getReservations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Reservation> getReservationsFromEntity(String entityId) {
		// TODO Auto-generated method stub
		return null;
	}
}
