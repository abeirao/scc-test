package scc.srv.api;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import scc.data.Calendar;
import scc.data.Forum;
import scc.data.Reservation;

@Path("/calendar")
public interface CalendarAPI {

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Calendar get(@PathParam("id") String id);
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Calendar create(Calendar calendar);	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Calendar delete(@PathParam("id") String id);

	@GET
	@Path("/available/{calendarId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Date[] getAvailablePeriods(String calendarId);

	@GET
	@Path("/reservations/{calendarId}")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<Reservation> getReservations(String calendarId);	
	
}
