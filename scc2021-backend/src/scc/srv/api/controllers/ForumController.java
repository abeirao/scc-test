package scc.srv.api.controllers;

import java.util.Iterator;

import scc.data.Forum;
import scc.data.Messsage;
import scc.srv.api.ForumAPI;
import scc.srv.api.services.ForumService;

public class ForumController implements ForumAPI {

	private ForumService forums;
	
	public ForumController() {
		this.forums = new ForumService();
	}

	@Override
	public Forum get(String id) {
		return forums.get(id);
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
	public Forum delete(String id) {
		return forums.delete(id);
	}

	@Override
	public Iterator<Forum> getForumByEntity(String entityId) {
		return forums.getForumByEntity(entityId);
	}
}
