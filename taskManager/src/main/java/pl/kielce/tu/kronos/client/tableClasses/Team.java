package pl.kielce.tu.kronos.client.tableClasses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.fabric.Server;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import pl.kielce.tu.kronos.client.TreeViewCreator;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;
import pl.kielce.tu.kronos.model.TreeViewAccess;


public class Team implements JSONAble, TreeViewAccess, CRUDAble{

	private int id;
	private String name;
	private int idTeamLeader;
	
	public Team(int id, String name, int idTeamLeader) {
		this.id = id;
		this.name = name;
		this.idTeamLeader = idTeamLeader;
	}
	
	public Team(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.name = obj.getString("name");
		this.idTeamLeader = obj.getInt("idTeamLeader");
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("name", this.name);
		obj.put("idTeamLeader", new Integer(this.idTeamLeader));
		return obj.toString();
	}
	
	public String getName() {
		return name;
	}

	public int getIdTeamLeader() {
		return idTeamLeader;
	}

	public static List<Team> getTeams(){
		ServerConnection connection;
		try {
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Team", "GET"));
			//System.out.println(response.getJSONdata());
			JSONArray array = new JSONArray(response.getJSONdata());
			List<Team> list = new ArrayList<>(20);
			for(Object elem: array){
				list.add(new Team((String)elem));
			}
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static void addTeamMember(int idTeam,int idPerson)
	{
		try
		{
		ServerConnection connection;
		connection = ServerConnection.getServer();
		JSONObject obj = new JSONObject();
		obj.put("id_person",idPerson);
		obj.put("id",idTeam);
		obj.put("member", "");
//		System.out.println("trying to add team member");
		Response response = connection.sendRequest(new Request("Team","POST",obj.toString()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public static void addTeam(int idTeamLeader, String name)
	{
		try{
		ServerConnection connection;
		connection = ServerConnection.getServer();
//		System.out.println("trying to add team");
		Response response = connection.sendRequest(new Request("Team","POST",new Team(0,name,idTeamLeader).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void editTeam(int idTeam, String name,int idTeamLeader)
	{
		try{
		ServerConnection connection;
		connection = ServerConnection.getServer();
//		System.out.println("trying to edit team");
		Response response = connection.sendRequest(new Request("Team","PUT",new Team(idTeam,name,idTeamLeader).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public static void removeTeam(int idTeam)
	{
		try{
		ServerConnection connection;
		connection = ServerConnection.getServer();
//		System.out.println("trying to remove team");
		Response response = connection.sendRequest(new Request("Team","DELETE",new Team(idTeam,"",0).JSONExport()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void removeTeamMember(int idTeam,int idPerson)
	{
		try
		{
		ServerConnection connection;
		connection = ServerConnection.getServer();
		JSONObject obj = new JSONObject();
		obj.put("id_person",idPerson);
		obj.put("id",idTeam);
		obj.put("member", "");
//		System.out.println("trying to remove team member");
		Response response = connection.sendRequest(new Request("Team","DELETE",obj.toString()));
//		System.out.println(response.getCode());
//		System.out.println(response.getJSONdata());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean add() {
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Team","POST", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update() {
		try{
			ServerConnection connection;
			connection = ServerConnection.getServer();
			Response response = connection.sendRequest(new Request("Team","PUT", this.JSONExport()));
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
			Response response = connection.sendRequest(new Request("Team","DELETE", this.JSONExport()));
			return response.getCode() == 200 ? true : false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
}
