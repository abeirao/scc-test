package scc.data;

import java.util.Date;
import java.util.Map;

public class Calendar {
	
	private String _rid;
    private String id;
    private String name;
    private String description;
    private Date[] availableDays;
    private Map <String, Reservation> reservations;
    private Map <Date, Reservation> calendarEntry;
    
	public String get_rid() {
		return _rid;
	}
	public void set_rid(String _rid) {
		this._rid = _rid;
	}
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date[] getAvailableDays() {
		return availableDays;
		//TODO
	}

	public void setAvailableDays(Date[] availableDays) {
		this.availableDays = availableDays;
	}

	public Map<String, Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Map<String, Reservation> reservations) {
		this.reservations = reservations;
	}

	public void setCalendarEntry(Map<Date, Reservation> calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public void putReservation(String entityId, Reservation reservation, Date day) { 
		reservations.put(entityId, reservation); 
		calendarEntry.put(day, reservation);
		
	}
	
	public Reservation getReservationById(String entityId) { return reservations.get(entityId); }

    public Map<Date, Reservation> getCalendarEntry() { return calendarEntry; }
}
