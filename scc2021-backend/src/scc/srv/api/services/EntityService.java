package scc.srv.api.services;


import com.azure.cosmos.implementation.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Forum;
import scc.data.Reservation;
import scc.exceptions.DayAlreadyOccupiedException;
import scc.redis.RedisCache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.NotFoundException;

public class EntityService {

    public static final String ENTITY_KEY_PREFIX = "entity: ";
    private static final String CALENDAR_KEY_PREFIX = "calendar:";

    ObjectMapper mapper = new ObjectMapper();

    private CosmosDBLayer cosmosDB;
    private CalendarService calendarService;
    private ReservationService reservationService;
    private ForumService forums;

    public EntityService() {
        cosmosDB = CosmosDBLayer.getInstance();
        calendarService = new CalendarService();
        reservationService = new ReservationService();
        forums = new ForumService();
    }

    public Iterator<Entity> getAll() {
        return cosmosDB.getAllEntities().iterator();
    }

    public Entity get(String id) throws NotFoundException {
        Entity entity;
        try {
   
            entity = cosmosDB.getEntity(id);
           
            return entity;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Entity create(Entity entity) {
        try {
            entity.setId(Utils.randomUUID().toString());
            // add to db
            cosmosDB.put(CosmosDBLayer.ENTITIES, entity);

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Entity delete(String id) throws NotFoundException {
        try {
            Entity entity = this.get(id);
            // delete from database
            cosmosDB.delete(CosmosDBLayer.ENTITIES, entity).getItem();
            return entity;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Entity update(Entity entity) {
        cosmosDB.update(CosmosDBLayer.ENTITIES, entity);

        return entity;
    }

    public void addMedia(String id, String mediaId) {
        Entity entity = cosmosDB.getEntity(id);
        String[] mediaIds = entity.getMediaIds();
        mediaIds = new String[mediaIds.length + 1];
        mediaIds[mediaIds.length - 1] = mediaId;

        this.update(entity); // update entity
    }


    public void createReservation(Reservation reservation) throws DayAlreadyOccupiedException {
        try {
            Entity entity = this.get(reservation.getEntityId());

            Calendar calendar = calendarService.get(entity.getCalendarId());
            List<Date> availableDays = calendar.getAvailableDays();
            Date day = null;
            int i = availableDays.indexOf(reservation.getDay());
            if (i == -1) {
                throw new DayAlreadyOccupiedException();
            }
            calendar.putReservation(reservation, reservation.getDay());
            reservationService.addReservation(reservation);
            calendarService.update(calendar);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Calendar createCalendar(Calendar calendar) {
        try {
            List<Date> availableDays = calendarService.computeAvailableDays();
            calendar.setAvailableDays(availableDays);
            calendar.setId(Utils.randomUUID().toString());
            cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
            Entity entity = this.get(calendar.getEntityId());
            entity.setCalendarId(calendar.getId());
            this.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }
}
