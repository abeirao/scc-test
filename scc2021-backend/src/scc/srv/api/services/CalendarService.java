package scc.srv.api.services;

import java.util.*;

import com.azure.cosmos.implementation.apachecommons.lang.tuple.Pair;
import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.CalendarAPI;

import javax.swing.text.html.HTMLDocument;

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
		Calendar calendar;
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
		//CosmosItemResponse response = cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		//if(response.getStatusCode() != 201){
			//rebentar isto tudo, faz sentido não ? adicionar isto em td o lado ?
		//}
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
		}  catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (calendar == null)
			calendar = cosmosDB.getCalendar(id);
		else
			jedis.del(CALENDAR_KEY_PREFIX + id);
		
		return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, calendar).getItem();
	}

	public Calendar update(Calendar calendar) {
		cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		try {
			jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return calendar;
	}


	public Date[] getAvailablePeriods(String calendarId){
		//ideia, e o stor fala "information about available periods", ou seja uma lista com periodos de datas ? que ele tem disponivel, pode ser uma valor default, tipo ves 30 dias do dia
		//	da querie, ou com queries personalizadas mas isso ]e to much pa mim
		//Isto só é possivel dps de perceber como é suposto date ser uma string e nao haver uma lista organizada por datas no calendar

		
		/*
		 * Dei esta try -> mas agora damos update a lista de availableDays qdo criamos uma reservation
		 */
		Calendar calendar = this.get(calendarId);
		
		try {
			calendar = mapper.readValue(jedis.get(CALENDAR_KEY_PREFIX + calendarId), Calendar.class);
		
			if (calendar == null) {
				calendar = cosmosDB.getCalendar(calendarId);
				jedis.set(CALENDAR_KEY_PREFIX + calendarId, mapper.writeValueAsString(calendar));
			}
			
			Date[] availableDays = calendar.getAvailableDays();
			return availableDays;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


	}

	public Iterator<Reservation> getReservations(String calendarId){
		//" a list of reservations." ou seja as reservations de um calendar
		Calendar calendar = this.get(calendarId);
		Map<String, Reservation> reservation = calendar.getReservations();
		return reservation.values().iterator();
	}

}
