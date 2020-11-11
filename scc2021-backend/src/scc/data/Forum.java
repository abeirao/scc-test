package scc.data;

import java.util.List;

public class Forum {

    private String id;
    private String entityId;
    private List<ForumMessage> messages;
	private String _rid;
	private String from;
    
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
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public List<ForumMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<ForumMessage> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "Forum: " + id + " by entity: " + entityId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

}
