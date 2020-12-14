package scc.srv.api;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import scc.data.Forum;
import scc.data.Message;

@Path("/forum")
public interface ForumAPI {
	
	final String ENDPOINT = "/forum";
	
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
	public Message addMessage(@PathParam("forumId") String forumId, Message newMessage);
	
	/**
	 * 
	 * @param forumId
	 * @param messageIdToReply
	 * @param newMessage
	 * @return
	 */
	@POST
	@Path("/reply/{forumId}/{messageIdToReply}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String reply(@PathParam("forumId") String forumId, @PathParam("messageIdToReply") String messageIdToReply, Message newMessage);
	
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
	@Path("/{entityId}/forums")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<Forum> getForumByEntity(@PathParam("entityId") String entityId);
}
