package scc.srv.api.controllers;

import java.util.Iterator;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import scc.data.Forum;
import scc.data.Message;
import scc.srv.api.ForumAPI;
import scc.srv.api.services.ForumService;

@Path(ForumAPI.ENDPOINT)
public class ForumController implements ForumAPI {

	private ForumService forums;

	public ForumController() {
		this.forums = new ForumService();
	}

	@Override
	public Forum get(String id) throws WebApplicationException {
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
	public Message addMessage(String forumId, Message newMessage) throws WebApplicationException  {
		try{
			return forums.addMessage(forumId, newMessage);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

	}

	@Override
	public String reply(String forumId, String messageIdToReply, Message newMessage) throws WebApplicationException  {
		try{
			return forums.reply(forumId, messageIdToReply, newMessage);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Override
	public Forum delete(String id) throws WebApplicationException  {
		try {
			return forums.delete(id);
		} catch (NotFoundException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Override
	public Iterator<Forum> getForumByEntity(String entityId) throws WebApplicationException {
		try {
			return forums.getForumByEntity(entityId);
		} catch (NotFoundException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
}
