package scc.data;

import java.util.*;

public class Calendar {
	
	private String _rid;
    private String id;
    private String name;
    private String description;
    private List<Date> availableDays;
    private Map <Date, String> calendarEntry;
    
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

	public List<Date> getAvailableDays() {
		return availableDays;
	}

	public void setAvailableDays(List<Date> availableDays) {
		Collections.sort(availableDays, new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				if(o1.equals(o2)){
					return 0;
				} else if(o1.after(o2)) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		this.availableDays = availableDays;
	}

	public Iterator<String> getReservations() {
		return calendarEntry.values().iterator();
	}

	public void setCalendarEntry(Map<Date, String> calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public void putReservation(Reservation reservation, Date day) {
		calendarEntry.put(day, reservation.getId());
		int i = availableDays.indexOf(day);
		if(i != -1) {
			availableDays.remove(i);
		}
	}

    public Map<Date, String> getCalendarEntry() { return calendarEntry; }

	@Override
	public String toString() {
		return "Calendar [_rid=" + _rid + ", id=" + id + ", name=" + name + ", description=" + description + ", availableDays=" + availableDays.toString() +
				", calendarEntry=" + calendarEntry.toString()  + "]";
	}
}
