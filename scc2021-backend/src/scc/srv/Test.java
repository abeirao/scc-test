package scc.srv;

import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import scc.data.Calendar;
import scc.data.Entity;
import scc.data.Forum;
import scc.data.Messsage;
import scc.data.Reservation;
import scc.exceptions.DayAlreadyOccupiedException;
import scc.redis.RedisCache;
import scc.srv.api.services.CalendarService;
import scc.srv.api.services.EntityService;
import scc.srv.api.services.ForumService;
import scc.srv.api.services.MediaService;
import scc.srv.api.services.ReservationService;
import redis.clients.jedis.Jedis;

public class Test {
	
	public static void main(String[] args) {
		
		try {	
			// create data			
			Calendar calendar = new Calendar();
			String calendarId = "0" + System.currentTimeMillis();
			calendar.setName("nice calendar");
			calendar.setCalendarEntry(new HashMap<Date, String>());
			calendar.setAvailableDays(new ArrayList<Date>());
			
			String entityId = "0" + System.currentTimeMillis();
			Entity ent = new Entity();
			ent.setName("SCC " + entityId);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			ent.setMediaIds(new String[] {"456"});
			ent.setCalendarId(calendarId);
			Reservation res = new Reservation();
			res.setName("very nice reservation");
			res.setDay(new SimpleDateFormat("dd/MM/yyyy").parse("18/11/2020"));
			res.setId("0" + System.currentTimeMillis());
			res.setEntityId(entityId);	

			Forum forum = new Forum();
			forum.setId("0" + System.currentTimeMillis());
			forum.setEntityId(ent.getId());
			forum.setMessages(new ArrayList<Messsage>());
			
			/* TEST METHOD CALLS */
			// test2();			
			// testRedis(ent, res); 
			testServices(ent, res, forum, calendar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void test2() {
		try {
			MediaService media = new MediaService();
			ObjectMapper mapper = new ObjectMapper();			
			
			String id = "0" + System.currentTimeMillis();
			Entity ent = new Entity();
			ent.setId(id);
			ent.setName("SCC " + id);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			ent.setMediaIds(new String[] {"456"});
			ent.setCalendarId("456");
			
			Date day = new SimpleDateFormat("\"dd/MM/yyyy\"").parse("29/11/2020");
			String resId = "1" + System.currentTimeMillis();
			Reservation reservation = new Reservation();
			reservation.setName("Teste");
			reservation.setDay(day);
			
			EntityService entities = new EntityService();
			
			ReservationService reservations = new ReservationService();
			
			entities.create(ent);
			reservations.addReservation(reservation);

			try (Jedis jedis = RedisCache.getCachePool().getResource()) {
			    Long cnt = jedis.lpush("MostRecentEntities", mapper.writeValueAsString(ent));
			    if (cnt > 5)
			        jedis.ltrim("MostRecentEntities", 0, 4);
			    
			    List<String> lst = jedis.lrange("MostRecentEntities", 0, -1);
			    for( String s : lst)
			    	System.out.println(s);
			    
			    cnt = jedis.incr("NumEntities");
			    System.out.println( "Num entities : " + cnt);
			}
			
			try (Jedis jedis = RedisCache.getCachePool().getResource()) {
			    Long cnt = jedis.lpush("MostRecentReservations", mapper.writeValueAsString(reservation));
			    if (cnt > 5)
			        jedis.ltrim("MostRecentReservations", 0, 4);
			    
			    List<String> lst = jedis.lrange("MostRecentReservations", 0, -1);
			    for( String s : lst)
			    	System.out.println(s);
			    
			    cnt = jedis.incr("NumReservations");
			    System.out.println( "NumReservations : " + cnt);
			}
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/* test services */
	public static void testServices(Entity ent, Reservation res, Forum forum, Calendar calendar) {
		try {
			System.out.println(" TESTING SERVICES ");
			
			EntityService entityService = new EntityService();
			ReservationService reservationService = new ReservationService();
			ForumService forumService = new ForumService();
			CalendarService calendarService = new CalendarService();


			System.out.println("Entity");
			Entity n = entityService.create(ent);
			System.out.println(n.toString());
			System.out.println(entityService.get(ent.getId()).toString());
			calendar.setEntityId(n.getId());

			System.out.println("Calendar");
			Calendar c = entityService.createCalendar(calendar);
			System.out.println(c.toString());
			System.out.println(calendar.toString());
			res.setEntityId(n.getId());

			System.out.println("Reservation");
			entityService.createReservation(res);
			calendar.setEntityId(n.getId());

		
			System.out.println(reservationService.addReservation(res).toString());
			System.out.println(reservationService.get(res.getId()).toString());
			
			System.out.println("Forum");
			System.out.println(forumService.create(forum).toString());
			System.out.println(forumService.get(forum.getId()).toString());
			System.out.println(forumService.getForumByEntity(ent.getId()).toString());
		
		} catch (DayAlreadyOccupiedException e) {
			e.printStackTrace();
		}
	
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
