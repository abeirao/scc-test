package scc.srv.api.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.srv.api.CalendarAPI;

public class CalendarService {
	
	private CosmosDBLayer cosmosDB;
	private Map<String, Calendar> calendars;

	public CalendarService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		calendars = new HashMap<>();
	}

	public Calendar get(String id) {
		Calendar calendar = calendars.get(id);
		if (calendar == null)
			calendar = cosmosDB.getCalendar(id);
		return calendar;
	}


	public Calendar create(Calendar calendar) {
		cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		calendars.put(calendar.getId(), calendar);
		return calendar;
	}

	public Calendar delete(String id) {
		Calendar calendar = calendars.get(id);
		if (calendar == null)
			calendar = cosmosDB.getCalendar(id);
		else
			calendars.remove(id);
		
		return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, calendar).getItem();
	}

	public Map<String, String> getCalendarEntry(String id, String date) {
		// TODO Auto-generated method stub
		// what is this ????
		
		return null;
	}

}
