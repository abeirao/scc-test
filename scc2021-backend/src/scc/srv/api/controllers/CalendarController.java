package scc.srv.api.controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Path;

import scc.data.Calendar;
import scc.data.Reservation;
import scc.srv.api.CalendarAPI;
import scc.srv.api.services.CalendarService;


@Path(CalendarAPI.ENDPOINT)
public class CalendarController implements CalendarAPI {
	
	private CalendarService calendars;
	
	public CalendarController() {
		this.calendars = new CalendarService();
	}

	@Override
	public Calendar get(String id) {
		return calendars.get(id);
	}

	@Override
	public Calendar create(Calendar calendar) {
		return calendars.create(calendar);
	}

	@Override
	public Calendar delete(String id) {
		return calendars.delete(id);
	}
	
	@Override
	public Calendar update(Calendar calendar) {
		return calendars.update(calendar);
	}

	@Override
	public Iterator<Date> getAvailablePeriods(String calendarId){
		return calendars.getAvailablePeriods(calendarId);
	}
	
	@Override 
	public Iterator<String> getReservations(String calendarId){
		return calendars.getReservations(calendarId);
	}

}
