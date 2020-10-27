package scc.srv.api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.ForumMessage;
import scc.srv.api.ForumResource;

public class ForumService implements ForumResource {
	
	private Map<String, Forum> forums;
	private CosmosDBLayer cosmosDB;
	
	public ForumService() {
		forums = new HashMap<>();
		cosmosDB =  CosmosDBLayer.getInstance();
	}
	
	@Override
	public Forum get(String id) {
		// TODO Auto-generated method stub
		return cosmosDB.getForum(id);
	}

	@Override
	public Forum create(Forum forum) {
		// TODO Auto-generated method stub
		cosmosDB.put(CosmosDBLayer.CALENDARS, forum);
		return null;
	}

	@Override
	public ForumMessage addMessage(String forumId, ForumMessage newMessage) {
		// TODO Auto-generated method stub
		Forum forum = cosmosDB.getForum(forumId);
		
		List<ForumMessage> messages = forum.getMessages();
		messages.add(newMessage);
		forum.setMessages(messages);
		
		cosmosDB.put(CosmosDBLayer.FORUMS, forum);
		
		return newMessage;
	}

	@Override
	public String reply(String forumId, ForumMessage messageToReply, ForumMessage newMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Forum delete(String id) {
		// TODO Auto-generated method stub
		return (Forum) cosmosDB.delete(CosmosDBLayer.FORUMS, id).getItem();
	}

}
