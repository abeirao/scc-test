package scc.srv.api.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import scc.data.Calendar;
import scc.data.CosmosDBLayer;
import scc.srv.api.CalendarResource;

public class CalendarService implements CalendarResource {
	
	private Map<String, Calendar> calendars;
	private CosmosDBLayer cosmosDB;

	// TODO
	public CalendarService() {
		calendars = new HashMap<>();
		cosmosDB =  CosmosDBLayer.getInstance();
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Calendar get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar create(Calendar calendar) {
		// TODO Auto-generated method stub
		cosmosDB.put(CosmosDBLayer.CALENDARS, calendar);
		return calendar;
	}

	@Override
	public Calendar delete(String id) {
		// TODO Auto-generated method stub
		return (Calendar) cosmosDB.delete(CosmosDBLayer.CALENDARS, id).getItem();
	}

	@Override
	public Map<String, String> getCalendarEntry(String id, String date) {
		// TODO Auto-generated method stub
		Date dateO = new Date(date);
		
		return null;
	}

}
