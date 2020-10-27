package scc.data;

public class Forum {

    private String id;
    private String entityId;
    private String[] messages;
	private String _rid;
    
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
	public String[] getMessages() {
		return messages;
	}
	public void setMessages(String[] messages) {
		this.messages = messages;
	}



}
