package pl.kielce.tu.kronos.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.kielce.tu.kronos.client.model.ConnectionProperties;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.tableClasses.JSONAble;
import pl.kielce.tu.kronos.client.tableClasses.Person;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;

public class ClientTest{
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("test starts");
		int port = 2222;
		String ip = "localhost";
		/*{
			Socket s = new Socket(ip, port);
			System.out.println("socket bangla");
			s.close();
		}*/
		ServerConnection server = ServerConnection.getServer(ip, port);
		try{
			Scanner console = new Scanner(System.in);
			System.out.println("logging in");
			Response r = server.sendRequest(new Request("Auth", "GET", new Person(0, "bartek", "bartek", "", false).JSONExport()));
			System.out.println("request send");
			System.out.println(r.getCode());
			System.out.println(r.getJSONdata());
			System.out.println("go on?");
		
			console.nextLine();
			ServerConnection s = server.getServer();
	//		JSONAble j = new Task(0, "Kupic chleb", "active", null, null, new Date(), 2, 2);
			JSONAble j = new Person(0, "test", "nieAdmin", "Jan Kowalski", false);
			System.out.println("Data send to server:");
			System.out.println(j.JSONExport());
			System.out.println("password: " + ((Person)j).getPassword());
			Request request = new Request("Person", "GET", "{}");
			r = s.sendRequest(request);
			System.out.println(r.getCode());
			System.out.println(r.getJSONdata());
			console.nextLine();
			console.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("test ends");
	}
}
