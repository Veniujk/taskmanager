package pl.kielce.tu.kronos.client.tableClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;

public class Person implements JSONAble, CRUDAble{

	private int id;
	private String username;
	private String password;
	private String name;
	private boolean isAdmin;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Person(int id, String username, String password, String name, boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.isAdmin = isAdmin;
	}
	
	public Person(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.username = obj.getString("username");
		//this.password = obj.getString("password");
		this.name = obj.getString("name");
		this.isAdmin = obj.getBoolean("admin");
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("username", this.username);
		obj.put("password", this.password);
		obj.put("name", this.name);
		obj.put("admin", new Boolean(this.isAdmin));
		return obj.toString();
	}

	public static List<Person> getPersons(int idProject)
	{
		ServerConnection connection;
		try
		{
			connection = ServerConnection.getServer();
//			System.out.println("trying to get persons");
			JSONObject obj = new JSONObject();
			
//			System.out.println(obj.put("idProject", idProject).toString());
			Response response=null;
			response = connection.sendRequest(new Request("Person","GET",obj.put("idProject", idProject).toString()));
//			System.out.println(response.getJSONdata());
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Person> list = new ArrayList<>(20);
			for(Object elem:array)
			{
				list.add(new Person(elem.toString()));
			}
			return list;
		}catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Person> getAllPersons()
	{
		ServerConnection connection;
		try
		{
			connection = ServerConnection.getServer();
//			System.out.println("trying to get ALL persons");
	
			Response response=null;
			response = connection.sendRequest(new Request("Person","GET","{}"));
//			System.out.println(response.getJSONdata());
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Person> list = new ArrayList<>(20);
			for(Object elem:array)
			{
				list.add(new Person(elem.toString()));
			}
			return list;
		}catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public static List<Person> getPersonsFromTeam(int idTeam)
	{
		ServerConnection connection;
		try
		{
			connection = ServerConnection.getServer();
			System.out.println("trying to get persons");
			JSONObject obj = new JSONObject();
			
//			System.out.println(obj.put("idTeam", idTeam).toString());
			Response response=null;
			response = connection.sendRequest(new Request("Person","GET",obj.put("idTeam", idTeam).toString()));
//			System.out.println(response.getJSONdata());
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Person> list = new ArrayList<>(20);
			for(Object elem:array)
			{
				list.add(new Person(elem.toString()));
			}
			return list;
		}catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	@Deprecated
	public static void editPerson(int id,String username,String password,String name,boolean isAdmin)
	{
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Person","PUT",new Person(id,username,password,name,isAdmin).JSONExport()));
//			System.out.println(response.getCode());
//			System.out.println("edit person response: "+response.getJSONdata());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Person){
			if(obj == null){
				return false;
			}
			if (((Person)obj).id == this.id){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	@Override
	public boolean update(){
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Person","PUT", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static void addPerson(String username,String password,String name,boolean isAdmin)
	{
		try{
		ServerConnection connnection;
		connnection = ServerConnection.getServer();
//		System.out.println("trying to add person");
		Response response = connnection.sendRequest(new Request("Person","POST",new Person(0,username,password,name,isAdmin).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}
	public static void deletePerson(int idPerson)
	{
		try{
		ServerConnection connection;
		connection = ServerConnection.getServer();
//		System.out.println("trying to delete person");
		Response response = connection.sendRequest(new Request("Person", "DELETE",new Person(idPerson,"","","",false).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public boolean add() {
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Person","POST", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
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
			Response response = connection.sendRequest(new Request("Person","DELETE", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
}
