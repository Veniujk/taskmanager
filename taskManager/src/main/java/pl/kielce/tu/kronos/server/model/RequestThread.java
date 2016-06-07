package pl.kielce.tu.kronos.server.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.mysql.jdbc.SocksProxySocketFactory;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;
import pl.kielce.tu.kronos.server.model.tableClasses.Auth;
import pl.kielce.tu.kronos.server.model.tableClasses.JSONAble;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;
import pl.kielce.tu.kronos.server.model.tableManipulators.MessageModel;
import pl.kielce.tu.kronos.server.model.tableManipulators.PersonModel;
import pl.kielce.tu.kronos.server.model.tableManipulators.ProjectModel;
import pl.kielce.tu.kronos.server.model.tableManipulators.TeamModel;
import pl.kielce.tu.kronos.server.model.tableManipulators.TaskModel;
/**
 * Thread responsible for executing single request.
 * @author Bartosz Piskiewicz
 *
 */
public class RequestThread implements Runnable{

	private Socket socket;
	private Person user;

	public RequestThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run(){
		try{
			try{
				ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
				while(true){			
					Request request = (Request)in.readObject();
//					System.out.println(request.getResource() );
//					System.out.println(request.getJSONdata());
					Response response = this.executeRequest(request);
//					System.out.println("response:");
//					System.out.println(response.getCode());
//					System.out.println(response.getJSONdata());
					out.writeObject(response);
					if(request.getResource().equals("Auth") && response.getCode() == 401){
						break;
					}
				}
			}finally{
				this.socket.close();
			}
		}catch(SocketException e){
			System.out.println("socket terminated");
		}catch(IOException | ClassNotFoundException e){
//			e.printStackTrace();
			System.out.println("socket closed");
		}
		System.out.println("tread is end");
	}
	
	private Response executeRequest(Request request){
		switch(request.getResource()){
		case "Auth":
			Auth auth = new Auth(request.getJSONdata());
			if(auth.isLogged()){
				this.user = auth.getPerson();
				return new Response((short)200, this.user.JSONExport());
			}else{
				return new Response((short)401);			
			}
		case "Person":
			return new PersonModel(request, this.user).serveRequest();
		case "Project":
			return new ProjectModel(request, this.user).serveRequest();
		case "Task":
			return new TaskModel(request, this.user).serveRequest();
		case "Team":
			return new TeamModel(request, this.user).serveRequest();
		case "Message":
			return new MessageModel(request, this.user).serveRequest();
		default:
			return new Response((short)400, "Invalid resource");
		}
	}
	
	public synchronized boolean checkUserId(int userId){
		if(this.user.getId() == userId){
			return true;
		}else{
			return false;
		}
	}
	
	public synchronized void logout(){
		try {
			this.socket.close();
		} catch (IOException e) {
			System.out.println("unable to force logout user");
//			e.printStackTrace();
		}
	}
	
}
