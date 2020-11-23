package scc.srv.api.controllers;

import java.util.Iterator;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import scc.data.Reservation;
import scc.srv.api.ReservationAPI;
import scc.srv.api.services.ReservationService;

@Path(ReservationAPI.ENDPOINT)
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
		try {
			return reservations.getReservation(id);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
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
