package pl.kielce.tu.kronos.client.model;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import pl.kielce.tu.kronos.client.tableClasses.Person;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;

public class User implements Serializable{
	private String username;
	private String password;
	private boolean loggedIn;
	public static int myID;
	public static boolean isAdmin;
	public static String userLogged;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public boolean login(){
		try {
			ServerConnection server = ServerConnection.getServer();
			Response response = server.sendRequest(new Request("Auth", "GET", new Person(0, this.username, this.password, "", false).JSONExport()));
			System.out.println("after authorization from serwer : "+ response.getJSONdata());
			if(response.getCode() != 200){
				return false;
			}
			Person user  = new Person(response.getJSONdata());
			myID=user.getId();
			isAdmin=user.isAdmin();
			userLogged = user.getUsername();
			if(response.getCode() == 200){
				return true;
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	

}
