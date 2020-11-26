package scc.srv.api.controllers;

import java.util.Iterator;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import scc.data.Forum;
import scc.data.Messsage;
import scc.srv.api.ForumAPI;
import scc.srv.api.services.ForumService;

@Path(ForumAPI.ENDPOINT)
public class ForumController implements ForumAPI {

	private ForumService forums;

	public ForumController() {
		this.forums = new ForumService();
	}

	@Override
	public Forum get(String id)  throws WebApplicationException {
		try {
			return forums.get(id);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Override
	public Forum create(Forum forum) {
		return forums.create(forum);
	}

	@Override
	public Messsage addMessage(String forumId, Messsage newMessage) {
		return forums.addMessage(forumId, newMessage);
	}

	@Override
	public String reply(String forumId, Messsage messageToReply, Messsage newMessage) {
		return forums.reply(forumId, messageToReply, newMessage);
	}

	@Override
	public Forum delete(String id)  throws WebApplicationException{
		try {
			return forums.delete(id);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Override
	public Iterator<Forum> getForumByEntity(String entityId) {
		return forums.getForumByEntity(entityId);
	}
}
