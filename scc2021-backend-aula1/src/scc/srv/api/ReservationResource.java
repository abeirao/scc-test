package scc.srv.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import scc.data.Reservation;

@Path("/reservations")
public interface ReservationResource {

	/**
	 * 
	 * @param reservation
	 * @return
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation addReservation(Reservation reservation);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation getReservation(@PathParam("id") String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation deleteReservation(@PathParam("id") String id);

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	Reservation[] getReservations();

	/**
	 * 
	 * @param entityId
	 * @return
	 */
	@GET
	@Path("/entity/{entityId}")
	@Produces(MediaType.APPLICATION_JSON)
	Reservation[] getReservationsFromEntity(@PathParam("entityId") String entityId);
		
}
