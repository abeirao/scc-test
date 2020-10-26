package scc.data;

public class Calendar {
    private String id;
    private String name;
    private String description;
    private String[] availableDays;
    private Reservation[] reservations;
    private Map <String, String> calendarEntry;

    private String getId(){return id;}
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String[] getAvailableDays() { return availableDays; }
    public void setAvailableDays(String[] availableDays ) { this.availableDays = availableDays; }
    public void setReservation(Reservation reservation ){ }
    public Reservation getReservationById(String entityId) {return reservations.getReservation(entityId)}

    public Map<String, String> getCalendarEntry() { return calendarEntry; }
}
