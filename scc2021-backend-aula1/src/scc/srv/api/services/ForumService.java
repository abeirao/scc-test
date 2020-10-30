package scc.srv.api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.ForumMessage;
import scc.srv.api.ForumResource;

public class ForumService implements ForumResource {
	
	private CosmosDBLayer cosmosDB;
	
	public ForumService() {
		cosmosDB =  CosmosDBLayer.getInstance();
	}
	
	@Override
	public Forum get(String id) {
		return cosmosDB.getForum(id);
	}

	@Override
	public Forum create(Forum forum) {
		cosmosDB.put(CosmosDBLayer.FORUMS, forum);
		return forum;
	}

	@Override
	public ForumMessage addMessage(String forumId, ForumMessage newMessage) {
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
		Forum forum = cosmosDB.getForum(forumId);
		
		return newMessage.getMsg();
	}

	@Override
	public Forum delete(String id) {
		return (Forum) cosmosDB.delete(CosmosDBLayer.FORUMS, id).getItem();
	}
	
	@Override
	public Forum getForumByEntity(String entityId) {
		return null; // TODO - query is cosmosDBLayer
		// return cosmosDB.getForumByEntity(entityId);
	}
	
	

}
