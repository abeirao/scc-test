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
import scc.data.*;
import scc.redis.RedisCache;

public class ForumService {

    public static final String FORUM_KEY_PREFIX = "forum: ";
    public static final String FORUM_ENTITY_KEY_PREFIX = "entityForum: ";

    ObjectMapper mapper = new ObjectMapper();

    private CosmosDBLayer cosmosDB;

    public ForumService() {
        cosmosDB = CosmosDBLayer.getInstance();
    }

    public Forum get(String id) throws NotFoundException {
        Forum forum = null;
        try {
            forum = cosmosDB.getForum(id);
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

        return forum;
    }

    public Messsage addMessage(String forumId, Messsage newMessage) {
        Forum forum = cosmosDB.getForum(forumId);
        newMessage.setId(Utils.randomUUID().toString());
        List<Messsage> messages = forum.getMessages();
        messages.add(newMessage);
        forum.setMessages(messages);

        cosmosDB.update(CosmosDBLayer.FORUMS, forum);
        

        return newMessage;
    }

    public String reply(String forumId, String messageIdToReply, Messsage newMessage) {
        Forum forum = cosmosDB.getForum(forumId);
        newMessage.setId(Utils.randomUUID().toString());
        newMessage.setReplyToId(messageIdToReply);
        List<Messsage> temp = forum.getMessages();
        temp.add(newMessage);

        forum.setMessages(temp);
        // update
        cosmosDB.update(CosmosDBLayer.FORUMS, forum);
        
        return newMessage.getMsg();
    }

    public Forum delete(String id) throws NotFoundException {
        try {
            Forum forum = this.get(id);
            // delete from database
            cosmosDB.delete(CosmosDBLayer.FORUMS, forum).getItem();
            return forum;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Iterator<Forum> getForumByEntity(String entityId) {
        return cosmosDB.getForumByEntity(entityId).iterator();
    }


}
