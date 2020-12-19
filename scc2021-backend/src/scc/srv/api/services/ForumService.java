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

    private CosmosDBLayer database;
    private Jedis jedis;

    public ForumService() {
        database = CosmosDBLayer.getInstance();
        jedis = RedisCache.getCachePool().getResource();
    }

    public Forum get(String id) throws NotFoundException {
        Forum forum = null;
        try {
            String object = jedis.get(FORUM_KEY_PREFIX + id);
            if (object != null) {
                forum = mapper.readValue(object, Forum.class);
            } else {
                forum = database.getForumById(id);
            }
            jedis.set(FORUM_KEY_PREFIX + id, mapper.writeValueAsString(forum));
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
        database.put(CosmosDBLayer.FORUMS, forum);
        try {
            jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
            jedis.sadd(FORUM_ENTITY_KEY_PREFIX + forum.getEntityId(), mapper.writeValueAsString(forum));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return forum;
    }

    public Message addMessage(String forumId, Message newMessage) {
        Forum forum = database.getForumById(forumId);
        newMessage.setId(Utils.randomUUID().toString());
        List<Message> messages = forum.getMessages();
        messages.add(newMessage);
        forum.setMessages(messages);

        database.update(CosmosDBLayer.FORUMS, forum);
        try {
            jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return newMessage;
    }

    public String reply(String forumId, String messageIdToReply, Message newMessage) {
        Forum forum = database.getForumById(forumId);
        newMessage.setId(Utils.randomUUID().toString());
        newMessage.setReplyToId(messageIdToReply);
        List<Message> temp = forum.getMessages();
        temp.add(newMessage);

        forum.setMessages(temp);
        // update
        database.update(CosmosDBLayer.FORUMS, forum);
        try {
            jedis.set(FORUM_KEY_PREFIX + forum.getId(), mapper.writeValueAsString(forum));
        } catch (JsonProcessingException e) {
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
            database.delete(CosmosDBLayer.FORUMS, forum);
            return forum;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
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
        return database.getForumByEntity(entityId).iterator();
    }


}
