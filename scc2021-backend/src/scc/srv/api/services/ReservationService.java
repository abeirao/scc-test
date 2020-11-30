package scc.srv.api.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.azure.cosmos.implementation.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import scc.data.CosmosDBLayer;
import scc.data.Entity;
import scc.data.Reservation;
import scc.redis.RedisCache;

public class ReservationService {

    public static final String RESERVATION_KEY_PREFIX = "reservation: ";
    public static final String RESERVATION_ENTITY_KEY_PREFIX = "entityReservations: ";

    ObjectMapper mapper = new ObjectMapper();

    private CosmosDBLayer cosmosDB;

    public ReservationService() {
        cosmosDB = CosmosDBLayer.getInstance();
    }

    public Reservation get(String id) throws NotFoundException {
        Reservation reservation = null;
        try {
            reservation = cosmosDB.getReservation(id);           
            return reservation;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Iterator<Reservation> getReservationsFromEntity(String entityId) throws NotFoundException {
    	try {
            return cosmosDB.getReservationsByEntity(entityId).iterator();

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Iterator<Reservation> getReservations() {
        return cosmosDB.getAllReservations().iterator();
    }

    public Reservation addReservation(Reservation reservation) {
        reservation.setId(Utils.randomUUID().toString());

        cosmosDB.put(CosmosDBLayer.RESERVATIONS, reservation);
        return reservation;
    }

    public Reservation update(Reservation reservation) {

        cosmosDB.update(CosmosDBLayer.RESERVATIONS, reservation);
        return reservation;
 
    }

    public Reservation delete(String id) throws NotFoundException {
        try {
            Reservation reservation = this.get(id);
            // delete reservation from database
            cosmosDB.delete(CosmosDBLayer.RESERVATIONS, reservation).getItem();
            return reservation;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
