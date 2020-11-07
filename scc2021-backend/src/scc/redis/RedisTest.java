package scc.redis;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Entity;
import scc.data.Reservation;
import scc.srv.api.services.EntityService;
import scc.srv.api.services.MediaService;
import scc.srv.api.services.ReservationService;

public class RedisTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
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
			ent.setCalendarIds(new String[] {"456"});
			
			String day = "Monday";
			String resId = "1" + System.currentTimeMillis();
			Reservation reservation = new Reservation();
			reservation.setId(resId);
			reservation.setName("Teste");
			reservation.setEntityId(ent.getId());
			reservation.setMedia(media);
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
}

