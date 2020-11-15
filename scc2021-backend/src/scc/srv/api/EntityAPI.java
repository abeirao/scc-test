package scc.srv.api;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import scc.data.Entity;
import scc.data.Reservation;

@Path("/entity")
public interface EntityAPI {
	
	static final String ENDPOINT = "/entity";

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Entity get(@PathParam("id") String id);
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Entity create(Entity entity);		
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Entity delete(@PathParam("id") String id);
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Entity update(Entity entity);

	/**
	 * 
	 * @param id
	 * @param mediaId
	 */
	@PUT
	@Path("/{id}/media/{mediaId}")
	@Consumes(MediaType.APPLICATION_JSON)
	void addMedia(@PathParam("id") String id, @PathParam("mediaId") String mediaId);

	/**
	 * 
	 * @param id
	 * @param reservation
	 */
	@POST
	@Path("/{id}/reservations")
	@Consumes(MediaType.APPLICATION_JSON)
	void createReservation(@PathParam("id") String id, Reservation reservation);

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<Entity> getAll();	
}
