package scc.srv.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import scc.data.Reservation;

import java.util.Iterator;
import java.util.List;

@Path("/reservation")
public interface ReservationAPI {
	
	final String ENDPOINT = "/reservation";

	/**
	 * Create a new reservation
	 * @param reservation
	 * @return
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation addReservation(Reservation reservation);
	
	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation updateReservation(Reservation reservation);
	
	/**
	 * Return a reservation by its id
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation getReservation(@PathParam("id") String id);
	
	/**
	 * Delete a reservation by its id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation deleteReservation(@PathParam("id") String id);

	/**
	 * Return all reservations in the system
	 * @return
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<Reservation> getReservations();

	/**
	 * Return all reservations for a given entity
	 * @param entityId
	 * @return
	 */
	@GET
	@Path("/entity/{entityId}")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<Reservation> getReservationsFromEntity(@PathParam("entityId") String entityId);

		
}
