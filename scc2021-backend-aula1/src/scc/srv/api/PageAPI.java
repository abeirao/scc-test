package scc.srv.api;

import scc.data.*;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public interface PageAPI {
	// API for page methods (frontpage...)
	
	@GET
	@Path("/entities")
	@Produces(MediaType.APPLICATION_JSON)
	List<Entity> popularEntities();
	
	@GET
	@Path("/entities/listed/{entityId}")
	@Produces(MediaType.TEXT_PLAIN)
	String listedEntity(@PathParam("entityId") String entityId);

	@GET
	@Path("/like/{entityId}")
	void setEntityLike(@PathParam("entityId") String entityId);
	
	@GET
	@Path("/stats")
	@Produces(MediaType.TEXT_PLAIN)
	String reservationStatistics();
}
