package scc.srv.api.controllers;

import java.util.Iterator;

import scc.data.Forum;
import scc.data.ForumMessage;
import scc.srv.api.ForumAPI;
import scc.srv.api.services.ForumService;

public class ForumController implements ForumAPI {

	private ForumService forums;
	
	public ForumController() {
		this.forums = new ForumService();
	}

	@Override
	public Forum get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Forum create(Forum forum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ForumMessage addMessage(String forumId, ForumMessage newMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reply(String forumId, ForumMessage messageToReply, ForumMessage newMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Forum delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Forum> getForumByEntity(String entityId) {
		// TODO Auto-generated method stub
		return null;
	}
}
