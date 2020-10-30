package scc.srv.api.controllers;

import java.util.Map;

import scc.data.Calendar;
import scc.srv.api.CalendarAPI;
import scc.srv.api.services.CalendarService;

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
	public Map<String, String> getCalendarEntry(String id, String date) {
		return calendars.getCalendarEntry(id, date);
	}
	
	

}