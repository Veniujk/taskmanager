package pl.kielce.tu.kronos.client.tableClasses;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import org.json.JSONObject;

import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Task implements JSONAble, Cloneable, CRUDAble {

	private int id;
	private String name;
	private String status;
	private Date createdAt;
	private Date updatedAt;
	private Date deadline;
	private int idPerson;
	private int idProject;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public Task(int id, String name, String status, Date createdAt, Date updatedAt, Date deadline, int idPerson,
			int idProject, String username) {

		this.id = id;
		this.name = name;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deadline = deadline;
		this.idPerson = idPerson;
		this.idProject = idProject;
		this.userName = username;
	}

	public Task(String JSONData) {
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.name = obj.getString("name");
		this.status = obj.getString("status");

		try {
			String dateParse = obj.getString("createdAt");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.createdAt = sdf.parse(dateParse);

			dateParse = obj.getString("updatedAt");
			this.updatedAt = sdf.parse(dateParse);

			dateParse = obj.getString("deadline");
			this.deadline = sdf.parse(dateParse);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.idPerson = obj.getInt("idPerson");
		this.idProject = obj.getInt("idProject");
		this.userName = obj.isNull("userName") ? "" : obj.getString("userName");

	}

	public static List<Task> getTasks(int idProject) {
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
//			System.out.println("trying to get data(tasks)");
			Response response = connection.sendRequest(
					new Request("Task", "GET", new Task(0, "", "", null, null, null, 0, idProject, null).JSONExport()));
			// System.out.println(response.getCode());
			System.out.println(response.getJSONdata().toString());
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Task> list = new ArrayList<>(20);
			for (Object elem : array) {
				list.add(new Task(elem.toString()));
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
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
		obj.put("username", this.userName);

		return obj.toString();
	}

	public static void sendTask(String taskName,Date deadline,Person person,int idProject)
	{
		try{
		ServerConnection connection;
		connection = ServerConnection.getServer();
//		System.out.println("trying to send data(task)");
		Response response = connection.sendRequest(new Request("Task","POST",new Task(0,taskName,"active",null,null,deadline,person.getId(),idProject,"").JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
	}catch(IOException e)
	{
		e.printStackTrace();
	}
	}
	@Deprecated
	public static void editTask(String taskName,Date deadline,Person person, int idProject)
	{
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
//			System.out.println("trying to edit data(task)");
//			Response response = connection.sendRequest(new Request("Task","PUT",new Task(MainController.idTaskClicked,taskName,"active",null,null,deadline,person.getId(),idProject,"").JSONExport()));
//			System.out.println(response.getCode());
//			System.out.println(response.getJSONdata());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public boolean update(){
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Task","PUT", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean add(){
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Task","POST", this.JSONExport()));
			if(response.getCode() == 200){
				JSONObject obj = new JSONObject(response.getJSONdata());
				this.id = obj.getInt("id");
				return true;
			}else{
				return false;
			}
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean remove() {
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Task","DELETE", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setParents(List<Integer> parents){
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			JSONObject obj = new JSONObject();
			obj.put("id", this.id);
			obj.put("childOf", parents);
			Response response = connection.sendRequest(new Request("Task","POST", obj.toString()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Integer> getParents(){
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			JSONObject obj = new JSONObject();
			obj.put("childOf", this.id);
			Response response = connection.sendRequest(new Request("Task","GET", obj.toString()));
			if(response.getCode() == 200){
				JSONArray array = new JSONArray(response.getJSONdata());
				List<Integer>  list =  new ArrayList<>();
				for(Object o: array){
					list.add( (Integer)o );
				}
				return list;
			}else{
				return null;
			}
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Object)new Task(this.id, this.name, this.status, this.createdAt, this.updatedAt, this.deadline, this.idPerson, this.idProject, this.userName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Task){
			if(this.id == ((Task)obj).id ){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return this.name;
	}
}
