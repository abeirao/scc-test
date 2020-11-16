package scc.data;

import java.util.Map;

public class Calendar {
	
	private String _rid;
    private String id;
    private String name;
    private String description;
    private String[] availableDays;
    private Map<String, Reservation> reservations;
    private Map <String, String> calendarEntry;
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

	public String[] getAvailableDays() {
		return availableDays;
	}

	public void setAvailableDays(String[] availableDays) {
		this.availableDays = availableDays;
	}

	public Map<String, Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Map<String, Reservation> reservations) {
		this.reservations = reservations;
	}

	public void setCalendarEntry(Map<String, String> calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public void putReservation(String entityId, Reservation reservation) { reservations.put(entityId, reservation); }
	
	public Reservation getReservationById(String entityId) { return reservations.get(entityId); }

    public Map<String, String> getCalendarEntry() { return calendarEntry; }
}
