package scc.srv.api.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.azure.cosmos.implementation.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Forum;
import scc.data.Messsage;
import scc.data.Reservation;
import scc.redis.RedisCache;

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
	
	public Forum get(String id) throws NotFoundException {
		Forum forum = null;
		try {
			forum = mapper.readValue(jedis.get(FORUM_KEY_PREFIX + id), Forum.class);
		
			if (forum == null) {
				forum = cosmosDB.getForum(id);
				jedis.set(FORUM_KEY_PREFIX + id, mapper.writeValueAsString(forum));
			}
			return forum;
		} catch (NotFoundException e) {
            throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Forum create(Forum forum) {
    	forum.setId(Utils.randomUUID().toString());
		cosmosDB.put(CosmosDBLayer.FORUMS, forum);
		try {
			jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
			jedis.sadd(FORUM_ENTITY_KEY_PREFIX + forum.getEntityId(), mapper.writeValueAsString(forum));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return forum;
	}

	public Messsage addMessage(String forumId, Messsage newMessage) {
		Forum forum = cosmosDB.getForum(forumId);
		
		List<Messsage> messages = forum.getMessages();
		messages.add(newMessage);
		forum.setMessages(messages);
		
		cosmosDB.update(CosmosDBLayer.FORUMS, forum);
		try {
			jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return newMessage;
	}

	public String reply(String forumId, Messsage messageToReply, Messsage newMessage) {
		Forum forum = cosmosDB.getForum(forumId);
		newMessage.set_replyToId(messageToReply.getId());
		List<Messsage> temp = forum.getMessages();
		temp.add(newMessage);

		forum.setMessages(temp);
		// update
		cosmosDB.update(CosmosDBLayer.FORUMS, forum);
		try {
			jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return newMessage.getMsg();
	}

	public Forum delete(String id) throws NotFoundException {
		try {
			Forum forum = this.get(id);
			// delete from cache
			jedis.del(FORUM_KEY_PREFIX + forum.getId());
			// delete forum from forums by entity on cache 
			jedis.srem(FORUM_ENTITY_KEY_PREFIX + forum.getEntityId(), mapper.writeValueAsString(forum));
			// delete from database
			return (Forum) cosmosDB.delete(CosmosDBLayer.FORUMS, forum).getItem();
		} catch (NotFoundException e) {
			throw e;
		} catch( Exception e) {
			e.printStackTrace();
			return null;
		}
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
