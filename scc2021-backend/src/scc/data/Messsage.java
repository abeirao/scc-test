package scc.data;

public class Messsage {
    private String id;
    private String forumId;
    private String msg;
	private String _rid;
	private String fromWho;
	private String idReply;

	public String getIdReply(){ return idReply; }
	public void set_idReply(String idReply){ this.idReply = idReply; }
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
	public String getForumId() {
		return forumId;
	}
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getFromWho() {
		return fromWho;
	}
	public void setFromWho(String fromWho) {
		this.fromWho = fromWho;
	}

    

}
