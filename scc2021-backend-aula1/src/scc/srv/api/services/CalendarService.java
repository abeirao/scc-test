package scc.srv.api.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.srv.api.CalendarResource;

public class CalendarService implements CalendarResource {
	
	private CosmosDBLayer cosmosDB;

	// TODO
	public CalendarService() {
		cosmosDB =  CosmosDBLayer.getInstance();
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Calendar get(String id) {
		return cosmosDB.getCalendar(id);
	}


	@Override
	public Calendar create(Calendar calendar) {
		cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		return calendar;
	}

	@Override
	public Calendar delete(String id) {
		return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, id).getItem();
	}

	@Override
	public Map<String, String> getCalendarEntry(String id, String date) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

}
