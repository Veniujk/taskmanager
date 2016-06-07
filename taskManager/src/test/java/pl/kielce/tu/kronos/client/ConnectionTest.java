package pl.kielce.tu.kronos.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;

public class ConnectionTest {

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Test starrted");
//		ServerConnection connection = ServerConnection.getServer("192.168.1.4", 2222);
		try(Socket socket = new Socket("169.254.44.53", 2222)){
			System.out.println("socket created");
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(new Request("Kefir", "best", new Person(1, "username", "pass", "fds", false).JSONExport()));
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("test end");
	}

}
