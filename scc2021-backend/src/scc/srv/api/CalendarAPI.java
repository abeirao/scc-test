package scc.srv.api;

import java.util.Date;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import scc.data.Calendar;

@Path("/calendar")
public interface CalendarAPI {
	
	final String ENDPOINT = "/calendar";

	/**
	 * 
	 * @param id calendarId
	 * @return Calendar
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Calendar get(@PathParam("id") String id) throws WebApplicationException ;
	
	/**
	 * 
	 * @param calendar to be created
	 * @return Calendar
	 */
	@POST
	@Path("/{entityId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Calendar create(Calendar calendar, @PathParam("entityId") String entityId);

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Calendar update(Calendar calendar);
	
	/**
	 * 
	 * @param id calendar Id
	 * @return calendar
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Calendar delete(@PathParam("id") String id) throws WebApplicationException;

	@GET
	@Path("/available/{calendarId}")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<Date> getAvailablePeriods(@PathParam("calendarId") String calendarId) throws WebApplicationException;

	@GET
	@Path("/reservations/{calendarId}")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<String> getReservations(@PathParam("calendarId") String calendarId) throws WebApplicationException;
	
}
