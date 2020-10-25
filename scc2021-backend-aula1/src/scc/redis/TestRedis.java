package scc.redis;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import scc.data.Entity;


/**
 * Hello world!
 *
 */
public class TestRedis
{
	public static void main(String[] args) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			String id = "0" + System.currentTimeMillis();
			Entity ent = new Entity();
			ent.setId(id);
			ent.setName("SCC " + id);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			ent.setMediaIds(new String[] {"456"});
			ent.setCalendarIds(new String[] {"456"});

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


