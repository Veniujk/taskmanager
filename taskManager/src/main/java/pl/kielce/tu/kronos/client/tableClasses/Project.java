package pl.kielce.tu.kronos.client.tableClasses;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;
import pl.kielce.tu.kronos.model.TreeViewAccess;

public class Project implements JSONAble, TreeViewAccess, CRUDAble{
	
	private int id;
	private String name;
	private int idTeam;
	


	public Project(int id, String name, int idTeam) {
		super();
		this.id = id;
		this.name = name;
		this.idTeam = idTeam;
	}
	
	public Project(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.name = obj.getString("name");
		this.idTeam = obj.getInt("idTeam");
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("name", this.name);
		obj.put("idTeam", new Integer(this.idTeam));
		return obj.toString();
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static List<Project> getProjects(int idTeam){
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
//			System.out.println("trying to get data (projects)");
			Response response = connection.sendRequest(new Request("Project", "GET", new Project(0, "", idTeam).JSONExport()));	
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Project> list = new ArrayList<>(20);
			for(Object elem: array){
				list.add(new Project((String)elem));
			}
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Deprecated
	public static void addProject(String name,int idTeam)
	{
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
		
//		System.out.println("trying to add project");
		Response response = connection.sendRequest(new Request("Project", "POST", new Project(0,name,idTeam).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean add() {
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Project", "POST", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean update() {
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Project", "PUT", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean remove() {
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Project", "DELETE", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		} catch (IOException e) {
			return false;
		}
	}
	public static void editProject(String name,int idTeam,int idProject)
	{
		ServerConnection connection;
		try{
		connection = ServerConnection.getServer();
//		System.out.println("trying to edit project");
		Response response = connection.sendRequest(new Request("Project","PUT",new Project(idProject,name,idTeam).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void deleteProject(int idProject)
	{
		ServerConnection connection;
		try{
		connection = ServerConnection.getServer();
//		System.out.println("trying to delete project");
		Response response = connection.sendRequest(new Request("Project","DELETE",new Project(idProject,"",0).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
