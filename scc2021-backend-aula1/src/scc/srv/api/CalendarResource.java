package scc.srv.api;

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

public interface CalendarResource {

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
	public Calendar create(Calendar entity);	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Calendar delete(@PathParam("id") String id);	
	
	/**
	 * 
	 * @param id
	 * @param date
	 * @return
	 */
	@GET
	@Path("/{id}/date/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> getCalendarEntry(@PathParam("id") String id, @PathParam("date") String date);
}
