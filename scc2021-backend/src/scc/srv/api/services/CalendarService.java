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
            cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
            jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
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
        cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
        try {
            jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    public Iterator<Date> getAvailablePeriods(String calendarId) {

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
