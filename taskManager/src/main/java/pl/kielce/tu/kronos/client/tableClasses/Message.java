package pl.kielce.tu.kronos.client.tableClasses;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;
import org.json.JSONObject;
import javafx.scene.control.TextArea;
import pl.kielce.tu.kronos.client.model.ServerConnection;

@SuppressWarnings("restriction")
public class Message implements JSONAble{

	private int id;
	private String content;
	private int idPerson;
	private int idTask;
	private Date createdAt;
	private String user;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public int getIdTask() {
		return idTask;
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Message(int id, String content, int idPerson, int idTask, Date createdAt, String user) {
		this.id = id;
		this.content = content;
		this.idPerson = idPerson;
		this.idTask = idTask;
		this.createdAt = createdAt;
		this.user=user;
	}
	
	public Message(int idTask, String message){
		this.idTask = idTask;
		this.content = message;
	}

	public Message(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.content = obj.getString("content");
		this.idPerson = obj.getInt("idPerson");
		this.idTask = obj.getInt("idTask");
		String dateParse = obj.getString("createdAt");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.createdAt =sdf.parse(dateParse);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.user= obj.getString("user");
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
	
	public static List<Message> getMessages(int idTask){
		ServerConnection connection;
		try{
			connection = ServerConnection.getServer();
//			System.out.println("trying to get data(messages)");
			Response response = connection.sendRequest(new Request("Message","GET",new Message(0,"",0,idTask,null,"").JSONExport()));
//			System.out.println(response.getJSONdata().toString());
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Message> list = new ArrayList<>(20);
			for(Object elem:array)
			{
				list.add(new Message(elem.toString()));
			}
			return list;
			}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Deprecated
	public static void sendMessage(TextArea textArea,int idTaskClicked)
	{
		if(idTaskClicked!=0)
		{
		ServerConnection connection;
		try{
			connection = ServerConnection.getServer();
//			System.out.println("trying to send data (messagee)");
			Response response = connection.sendRequest(new Request("Message","POST",new Message(0,textArea.getText(),0,idTaskClicked,null,"").JSONExport()));
//			System.out.println(response.getCode());
//			System.out.println(response.getJSONdata().toString());
			}catch(IOException e)
		{
				e.printStackTrace();
		}
		}
		else
		{
			System.out.println("Zadne zadanie nie zostalo zaznaczone!");
		}
	}

	public boolean send(){
		ServerConnection connection;
		try{
			connection = ServerConnection.getServer();
	//		System.out.println("trying to send data (messagee)");
			Response response = connection.sendRequest(new Request("Message","POST",new Message(this.idTask, this.content).JSONExport()));
			if(response.getCode() == (short)200){
				return true;
			}else{
				return false;
			}
		}catch(IOException e){
				e.printStackTrace();
		}
		return false;
	}
}
