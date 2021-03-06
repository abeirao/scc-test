package scc.srv.api.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.azure.cosmos.implementation.Utils;
import com.azure.cosmos.implementation.apachecommons.lang.tuple.Pair;
import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Database;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;
import scc.srv.api.CalendarAPI;

import javax.swing.text.html.HTMLDocument;
import javax.ws.rs.NotFoundException;

public class CalendarService {

    public static final String CALENDAR_KEY_PREFIX = "calendar: ";

    ObjectMapper mapper = new ObjectMapper();

    private Database database;
    private Jedis jedis;

    public CalendarService() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        database = new Database();
        jedis = RedisCache.getCachePool().getResource();
    }

    public Calendar get(String id) throws NotFoundException {
        Calendar calendar;
        try {
            String object = jedis.get(CALENDAR_KEY_PREFIX + id);
            if (object != null) {
                calendar = mapper.readValue(object, Calendar.class);
            } else {
                calendar = database.getCalendarById(id);
                jedis.set(CALENDAR_KEY_PREFIX + id, mapper.writeValueAsString(calendar));
            }
            return calendar;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * calculates the number of available days, which will be the days until the end of the respective month
     *
     * @return a list of available dates
     */
    public List<Date> computeAvailableDays() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(1);
        long numOfDays = ChronoUnit.DAYS.between(today, endDate);
        List<LocalDate> listOfDates = LongStream.range(0, numOfDays)
                .mapToObj(today::plusDays)
                .collect(Collectors.toList());
        List<Date> availableDays = new LinkedList<Date>();
        for (LocalDate date : listOfDates)
            availableDays.add(Date.from(date.atStartOfDay(defaultZoneId).toInstant()));

        return availableDays;
    }

    public Calendar delete(String id) throws NotFoundException {
        Calendar calendar = null;
        try {
            calendar = mapper.readValue(jedis.get(CALENDAR_KEY_PREFIX + id), Calendar.class);

            if (calendar == null)
                calendar = database.getCalendarById(id);
            else
                jedis.del(CALENDAR_KEY_PREFIX + id);

            database.delCalendar(calendar);
            return calendar;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Calendar update(Calendar calendar) {
        database.updateCalendar(calendar);
        try {
            jedis.set(CALENDAR_KEY_PREFIX + calendar.getId(), mapper.writeValueAsString(calendar));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    public Iterator<Date> getAvailablePeriods(String calendarId) {
        Calendar calendar = this.get(calendarId);
        List<Date> availableDays = calendar.getAvailableDays();
        return availableDays.iterator();
    }

    public Iterator<String> getReservations(String calendarId) {
        // returns the reservations in a given calendar
        Calendar calendar = this.get(calendarId);
        return calendar.getReservations();

    }

}
