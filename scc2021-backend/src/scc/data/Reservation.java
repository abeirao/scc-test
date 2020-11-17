package scc.data;

import java.util.Arrays;
import java.util.Date;

import scc.srv.api.services.MediaService;

public class Reservation {
    private String name;
    private Date day;
    private String id;
    private String entityId;
	private String _rid;

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
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
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

	
	@Override
	public String toString() {
		return "Reservation [_rid=" + _rid + ", id=" + id + ", name=" + name + ", entityId=" + entityId;
	}
}
