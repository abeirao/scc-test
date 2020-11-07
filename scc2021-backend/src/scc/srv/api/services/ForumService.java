package scc.srv.api.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.ForumMessage;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.ForumAPI;

public class ForumService  {
	
	public static final String FORUM_KEY_PREFIX = "forum: ";
	public static final String FORUM_ENTITY_KEY_PREFIX = "entityForum: ";
	
	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;
	
	public ForumService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}
	
	public Forum get(String id) {
		Forum forum = null;
		try {
			forum = mapper.readValue(jedis.get(FORUM_KEY_PREFIX + id), Forum.class);
		
			if (forum == null) {
				forum = cosmosDB.getForum(id);
				jedis.set(FORUM_KEY_PREFIX + id, mapper.writeValueAsString(forum));
			}
			return forum;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Forum create(Forum forum) {
		cosmosDB.put(CosmosDBLayer.FORUMS, forum);
		try {
			jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
			jedis.sadd(FORUM_ENTITY_KEY_PREFIX + forum.getEntityId(), mapper.writeValueAsString(forum));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forum;
	}

	public ForumMessage addMessage(String forumId, ForumMessage newMessage) {
		Forum forum = cosmosDB.getForum(forumId);
		
		List<ForumMessage> messages = forum.getMessages();
		messages.add(newMessage);
		forum.setMessages(messages);
		
		cosmosDB.put(CosmosDBLayer.FORUMS, forum);
		try {
			jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newMessage;
	}

	public String reply(String forumId, ForumMessage messageToReply, ForumMessage newMessage) {
		Forum forum = cosmosDB.getForum(forumId);
		// TODO 
		
		
		// update
		cosmosDB.put(CosmosDBLayer.FORUMS, forum);
		try {
			jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newMessage.getMsg();
	}

	public Forum delete(String id) {
		Forum forum = this.get(id);
		jedis.del(FORUM_KEY_PREFIX + forum.getId());
		// delete forum from forums by entity on cache TODO
		
		return (Forum) cosmosDB.delete(CosmosDBLayer.FORUMS, forum).getItem();
	}
	
	public Iterator<Forum> getForumByEntity(String entityId) {
		Set<Forum> forums = new HashSet<Forum>();
        Set<String> s = jedis.smembers(FORUM_ENTITY_KEY_PREFIX + entityId);
        if (s != null) {
	        s.forEach(x -> {
	            try {
	            	forums.add(mapper.readValue(x, Forum.class));
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        });
	        return forums.iterator();
        }	
		return cosmosDB.getForumByEntity(entityId).iterator();
	}
	
	

}
