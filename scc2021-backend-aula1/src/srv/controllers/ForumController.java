package srv.controllers;

import scc.data.Forum;
import scc.data.ForumMessage;
import scc.srv.api.ForumResource;

public class ForumController implements ForumResource {

	@Override
	public Forum get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String create(Forum entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addMessage(String forumId, ForumMessage newMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reply(String forumId, ForumMessage messageToReply, ForumMessage newMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
