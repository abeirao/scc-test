package scc.srv.api.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.srv.api.CalendarResource;

public class CalendarService implements CalendarResource {
	
	private CosmosDBLayer cosmosDB;
	private Map<String, Calendar> calendars;

	public CalendarService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		calendars = new HashMap<>();
	}

	// TODO add caching

	@Override
	public Calendar get(String id) {
		Calendar calendar = calendars.get(id);
		if (calendar == null)
			calendar = cosmosDB.getCalendar(id);
		return calendar;
	}


	@Override
	public Calendar create(Calendar calendar) {
		cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		calendars.put(calendar.getId(), calendar);
		return calendar;
	}

	@Override
	public Calendar delete(String id) {
		Calendar calendar = calendars.get(id);
		if (calendar == null)
			calendar = cosmosDB.getCalendar(id);
		else
			calendars.remove(id);
		
		return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, calendar).getItem();
	}

	@Override
	public Map<String, String> getCalendarEntry(String id, String date) {
		// TODO Auto-generated method stub
		// what is this ????
		
		return null;
	}

}
