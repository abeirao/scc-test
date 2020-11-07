package scc.data;

import scc.srv.api.services.MediaService;

public class Reservation {
    private String name;
    private String day;
    private String id;
    private String entityId;
	private String _rid;
	private MediaService media;

	public String get_rid() {
		return _rid;
	}
	public void set_rid(String _rid) {
		this._rid = _rid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public MediaService getMedia() {
		return media;
	}
	public void setMedia(MediaService media) {
		this.media = media;
	}
	
	public String toString(Reservation reservation) {
		// TODO
		return null;
	}
	public static Reservation fromString(String string) {
		// TODO Auto-generated method stub
		return null;
	}




}
