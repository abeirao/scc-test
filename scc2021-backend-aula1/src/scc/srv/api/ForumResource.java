package scc.srv.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import scc.data.Forum;
import scc.data.ForumMessage;

@Path("/forum")
public interface ForumResource {
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Forum get(@PathParam("id") String id);
	
	/**
	 * 
	 * @param forum
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Forum create(Forum forum);	
	
	/**
	 * 
	 * @param forumId
	 * @param newMessage
	 * @return
	 */
	@POST
	@Path("/new/{forumId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ForumMessage addMessage(@PathParam("forumId") String forumId, ForumMessage newMessage);
	
	/**
	 * 
	 * @param forumId
	 * @param messageToReply
	 * @param newMessage
	 * @return
	 */
	@POST
	@Path("/reply/{forumId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String reply(@PathParam("forumId") String forumId, ForumMessage messageToReply, ForumMessage newMessage);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Forum delete(@PathParam("id") String id);

	/**
	 * 
	 * @param entityId
	 * @return
	 */
	@GET
	@Path("/{id}/forums")
	@Produces(MediaType.APPLICATION_JSON)
	Forum getForumByEntity(@PathParam("entityId") String entityId);
}
