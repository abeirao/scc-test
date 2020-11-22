package scc.srv.api.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.azure.cosmos.implementation.apachecommons.lang.tuple.Pair;
import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        cosmosDB = CosmosDBLayer.getInstance();
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
            e.printStackTrace();
            return null;
        }
    }


    public Calendar create(Calendar calendar) {
        try {
        	List<Date> availableDays = this.computeAvailableDays();        	
        	calendar.setAvailableDays(availableDays);
        	
            cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
            jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    /**
     * calculates the number of available days, which will be the days until the end of the respective month
     * @return a list of available dates
     */
    private List<Date> computeAvailableDays() {    	
    	LocalDate today = LocalDate.now();
    	LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());           
		long numOfDays = ChronoUnit.DAYS.between(today, endDate);	    
		List<LocalDate> listOfDates = LongStream.range(0, numOfDays)
		                                .mapToObj(today::plusDays)
		                                .collect(Collectors.toList());	
		ZoneId defaultZoneId = ZoneId.systemDefault();
		List<Date> availableDays = new LinkedList<Date>();		
		for (LocalDate date: listOfDates) 
			availableDays.add(Date.from(date.atStartOfDay(defaultZoneId).toInstant()));
		
		return availableDays;		
	}

	public Calendar delete(String id) {
        Calendar calendar = null;
        try {
            calendar = mapper.readValue(jedis.get(CALENDAR_KEY_PREFIX + id), Calendar.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (calendar == null)
            calendar = cosmosDB.getCalendar(id);
        else
            jedis.del(CALENDAR_KEY_PREFIX + id);

        return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, calendar).getItem();
    }

    public Calendar update(Calendar calendar) {
        cosmosDB.update(CosmosDBLayer.CALENDARS, calendar);
        try {
            jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    public Iterator<Date> getAvailablePeriods(String calendarId) {
//TODO
        /*
         * Dei esta try -> mas agora damos update a lista de availableDays qdo criamos uma reservation
         */
        Calendar calendar = this.get(calendarId);
        if (calendar == null) {
            return null;
        }
            List<Date> availableDays = calendar.getAvailableDays();

            return availableDays.iterator();
    }

    public Iterator<String> getReservations(String calendarId) {
        // returns the reservations in a given calendar
        Calendar calendar = this.get(calendarId);
        return calendar.getReservations();

    }

}
