package pl.kielce.tu.kronos.client.model;

import java.beans.Transient;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;


/**
 * 
 * @author Bartosz Piskiewicz
 * Class responsible for connection with server application
 */
public class ServerConnection implements Closeable {

	private static ServerConnection server = null;
	private static String ip;
	private static int port;

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private ServerConnection() throws UnknownHostException, IOException{
		this.socket = new Socket(ServerConnection.ip, ServerConnection.port);
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(this.socket.getInputStream());
	}
	
	/**
	 * Gets an instance of ServerConnection class. Creates one if no such object exists.
	 * @return returns ServerConnection object
	 * @throws UnknownHostException when host is incorrect or wasn't specified before first use in programme.
	 * @throws IOException
	 */
	public static ServerConnection getServer() throws UnknownHostException, IOException{
		if(ServerConnection.ip == null || ServerConnection.port == 0){
			throw new UnknownHostException();
		}
		if(ServerConnection.server == null){
			System.out.println("socket is not created yet");
			ServerConnection.server = new ServerConnection();
		}
		return ServerConnection.server;
	}
	
	/**
	 * Gets an instance of ServerConnection class. Creates one if no such object exists.
	 * @param ip ip of server application.
	 * @param port port, which server application is listening on.
	 * @return Returns ServerConnection Object
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static ServerConnection getServer(String ip, int port) throws UnknownHostException, IOException{
		ServerConnection.ip = ip;
		ServerConnection.port = port;
		return ServerConnection.getServer();
	}
	
	@Override
	public void close() throws IOException  {
		System.out.println("zamykam socket");
		this.socket.close();
		ServerConnection.server = null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.socket.close();
		ServerConnection.server = null;
		super.finalize();
	}
	
	public synchronized Response sendRequest(Request request) throws IOException{
		try{
			this.out.writeObject(request);
			Response r = (Response)in.readObject();
			return r;
			
		}catch(Exception e){	//		na pokemona, do zmiany
			e.printStackTrace();
		}
		throw new IOException();
	}
	
}
