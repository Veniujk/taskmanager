package pl.kielce.tu.kronos.server.model.tableClasses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class Task implements JSONAble{

	private int id;
	private String name;
	private String status;
	private Date createdAt;
	private Date updatedAt;
	private Date deadline;
	private int idPerson;
	private int idProject;
	private String userName;
	
	

	/*public Task(int id, String name, String status, Date createdAt, Date updatedAt, Date deadline, int idPerson,
			int idProject) {
		
		this.id = id;
		this.name = name;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deadline = deadline;
		this.idPerson = idPerson;
		this.idProject = idProject;
	}*/
	
	

	public Task(int id, String name, String status, Date createdAt, Date updatedAt, Date deadline, int idPerson,
			int idProject, String userName) {
		
		this.id = id;
		this.name = name;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deadline = deadline;
		this.idPerson = idPerson;
		this.idProject = idProject;
		this.userName = userName;
	}

	public Task(String JSONData) throws JSONException, ParseException{
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.name = obj.isNull("name")? "" : obj.getString("name");
		this.status =  obj.isNull("status")? "" : obj.getString("status");
		if(!obj.isNull("deadline")){
			DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			this.deadline = format.parse(obj.getString("deadline"));
		}
//		this.name = obj.getString("name");
		this.idProject = obj.getInt("idProject");
		this.idPerson = obj.getInt("idPerson");
		if(!obj.isNull("updatedAt")){
			DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			this.updatedAt = format.parse(obj.getString("updatedAt"));
		}
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("name", this.name);
		obj.put("idProject", new Integer(this.idProject));
		obj.put("idPerson", new Integer(this.idPerson));
		obj.put("status", this.status);
		obj.put("createdAt", this.createdAt);
		obj.put("updatedAt", this.updatedAt);
		obj.put("deadline", this.deadline);
		obj.put("username",this.userName);
		return obj.toString();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Date getDeadline() {
		return deadline;
	}

	public int getIdPerson() {
		return idPerson;
	}

	public int getIdProject() {
		return idProject;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
