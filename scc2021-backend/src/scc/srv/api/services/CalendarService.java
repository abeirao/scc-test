package scc.srv.api.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.redis.RedisCache;
import scc.srv.api.CalendarAPI;

public class CalendarService {
	
	public static final String CALENDAR_KEY_PREFIX = "calendar: ";
	
	ObjectMapper mapper = new ObjectMapper();
	
	private CosmosDBLayer cosmosDB;
	private Jedis jedis;

	public CalendarService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		jedis = RedisCache.getCachePool().getResource();
	}

	public Calendar get(String id) {
		Calendar calendar = null;
		try {
			calendar = mapper.readValue(jedis.get(CALENDAR_KEY_PREFIX + id), Calendar.class);
		
			if (calendar == null) {
				calendar = cosmosDB.getCalendar(id);
				jedis.set(CALENDAR_KEY_PREFIX + id, mapper.writeValueAsString(calendar));
			}
			return calendar;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public Calendar create(Calendar calendar) {
		cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		try {
			jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar;
	}

	public Calendar delete(String id) {
		Calendar calendar = null;
		try {
			calendar = mapper.readValue(jedis.get(CALENDAR_KEY_PREFIX + id), Calendar.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (calendar == null)
			calendar = cosmosDB.getCalendar(id);
		else
			jedis.del(CALENDAR_KEY_PREFIX + id);
		
		return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, calendar).getItem();
	}

	public Map<String, String> getCalendarEntry(String id, String date) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
