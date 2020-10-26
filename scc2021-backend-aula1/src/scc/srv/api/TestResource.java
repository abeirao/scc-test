package scc.srv.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class TestResource // only for testing
{

	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "v: 0001";
	}
	
	@Path("/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "test OK";
	}
}
