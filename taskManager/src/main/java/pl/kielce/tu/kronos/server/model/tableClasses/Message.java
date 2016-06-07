package pl.kielce.tu.kronos.server.model.tableClasses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements JSONAble{

	private int id;
	private String content;
	private int idPerson;
	private int idTask;
	private Date createdAt;
	private String user;
	
	public Message(int id, String content, int idPerson, int idTask, Date createdAt) {
		this.id = id;
		this.content = content;
		this.idPerson = idPerson;
		this.idTask = idTask;
		this.createdAt = createdAt;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Message(int id, String content, int idPerson, int idTask, Date createdAt, String user) {
		this.id = id;
		this.content = content;
		this.idPerson = idPerson;
		this.idTask = idTask;
		this.createdAt = createdAt;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public int getIdPerson() {
		return idPerson;
	}

	public int getIdTask() {
		return idTask;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Message(String JSONData) throws JSONException, ParseException{
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.content = obj.getString("content");
		this.idPerson = obj.getInt("idPerson");
		this.idTask = obj.getInt("idTask");
		if(!obj.isNull("createdAt")){
			DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			this.createdAt = format.parse(obj.getString("createdAt"));		
		}
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("content", this.content);
		obj.put("idPerson", new Integer(this.idPerson));
		obj.put("idTask", new Integer(this.idTask));
		obj.put("createdAt", this.createdAt);
		obj.put("user", this.user);
		return obj.toString();
	}	
}
