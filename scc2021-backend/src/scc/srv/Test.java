package scc.srv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import scc.data.Calendar;
import scc.data.Entity;
import scc.data.Forum;
import scc.data.ForumMessage;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.services.CalendarService;
import scc.srv.api.services.EntityService;
import scc.srv.api.services.ForumService;
import scc.srv.api.services.ReservationService;
import redis.clients.jedis.Jedis;

public class Test {
	
	public static void main(String[] args) {
		
		try {
			Calendar calendar = new Calendar();
			String calendarId = "0" + System.currentTimeMillis();
			calendar.setId(calendarId);
			calendar.setName("nice calendar");
			

			String entityId = "0" + System.currentTimeMillis();
			Entity ent = new Entity();
			ent.setId(entityId);
			ent.setName("SCC " + entityId);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			ent.setMediaIds(new String[] {"456"});
			ent.setCalendarId(calendarId);

			Reservation res = new Reservation();
			res.setName("very nice reservation");
			res.setDay("18/11/2020");
			res.setId("0" + System.currentTimeMillis());
			res.setEntityId(entityId);
			

			Forum forum = new Forum();
			forum.setId("0" + System.currentTimeMillis());
			forum.setEntityId(ent.getId());
			forum.setMessages(new ArrayList<ForumMessage>());
			forum.setFrom("john");
			
			// testRedis(ent, res); TESTED, works
			testServices(ent, res, forum, calendar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* test services */
	public static void testServices(Entity ent, Reservation res, Forum forum, Calendar calendar) {
		System.out.println(" TESTING SERVICES ");
		EntityService entityService = new EntityService();
		ReservationService reservationService = new ReservationService();
		ForumService forumService = new ForumService();
		CalendarService calendarService = new CalendarService();
		
		System.out.println("Calendar");
		System.out.println(calendarService.create(calendar));

		System.out.println("Entity");
		System.out.println(entityService.create(ent).toString());
		System.out.println(entityService.get(ent.getId()).toString());
		entityService.createReservation(ent.getId(), res);

		System.out.println("Reservation");
		//System.out.println(reservationService.addReservation(res).toString());
		System.out.println(reservationService.getReservation(res.getId()).toString());
		
		System.out.println("Forum");
		System.out.println(forumService.create(forum).toString());
		System.out.println(forumService.get(forum.getId()).toString());
		System.out.println(forumService.getForumByEntity(ent.getId()).toString());
		
		
	
	}
	
	/* test redis */
	public static void testRedis(Entity ent, Reservation res) {
		System.out.println(" TESTING REDIS ");
		ObjectMapper mapper = new ObjectMapper();
		
		try (Jedis jedis = RedisCache.getCachePool().getResource()) {
			
			// add entity to redis
			jedis.set(EntityService.ENTITY_KEY_PREFIX + ent.getId(), mapper.writeValueAsString(ent));		
			System.out.println("entity in jedis: " + mapper.readValue(jedis.get(EntityService.ENTITY_KEY_PREFIX + ent.getId()), Entity.class).toString());
			
			// add reservation to redis
			jedis.set(ReservationService.RESERVATION_KEY_PREFIX + res.getId(), mapper.writeValueAsString(res));		
			System.out.println("reservation in jedis: " + mapper.readValue(jedis.get(ReservationService.RESERVATION_KEY_PREFIX + res.getId()), Reservation.class).toString());
			
			// add reservation to entity on redis
			jedis.sadd(ReservationService.RESERVATION_ENTITY_KEY_PREFIX + res.getEntityId(), mapper.writeValueAsString(res));			
			// get reservations by entity from set on cache
			final Set<Reservation> reservations = new HashSet<Reservation>();
	        Set<String> s = jedis.smembers(ReservationService.RESERVATION_ENTITY_KEY_PREFIX + res.getEntityId());
	        if (s != null) {
		        s.forEach(x -> {
		            try {
		                reservations.add(mapper.readValue(x, Reservation.class));
		            } catch (JsonProcessingException e) {
		                e.printStackTrace();
		            }
		        });		        
	        }
	        Iterator<Reservation> it = reservations.iterator();
			System.out.println("reservations by entity: ");
			while(it.hasNext()) {
				System.out.println(it.next().toString());
			}
			
			// remove reservation from reservations by entity on cache
			jedis.srem(ReservationService.RESERVATION_ENTITY_KEY_PREFIX + res.getEntityId(), mapper.writeValueAsString(res));
			// get reservations by entity from set on cache
			Set<Reservation> ress = new HashSet<Reservation>();
			s = jedis.smembers(ReservationService.RESERVATION_ENTITY_KEY_PREFIX + res.getEntityId());
	        if (s != null) {
		        s.forEach(x -> {
		            try {
		            	ress.add(mapper.readValue(x, Reservation.class));
		            } catch (JsonProcessingException e) {
		                e.printStackTrace();
		            }
		        });		        
	        }
	        it = ress.iterator();
			System.out.println("reservations by entity (after delete - should print nothing): ");
			while(it.hasNext()) {
				System.out.println(it.next().toString());
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
