package pl.kielce.tu.kronos.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.io.InputStream;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.client.tableClasses.Message;


public class Client 
{
	private String ip;
	private Integer port;
	private InputStream inputStream;
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		Client client1 = new Client();
		client1.getConfigConnection();
		client1.connect();
	}
	private void connect() throws IOException, ClassNotFoundException
	{
		Socket socket = new Socket(this.ip,this.port);
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream objectOutput= new ObjectOutputStream(socket.getOutputStream());
		//odczytaj
		Message messageInput = (Message)objectInput.readObject();
//		System.out.println(messageInput.getContent());
		//wyslij
	//	Request request = new Request();
	//	request.setRequest("213");
	//	objectOutput.writeObject(request);
		
		socket.close();
	}
	private void getConfigConnection () throws IOException
	{
	
		Properties config = new Properties();
		String configFileName="connectionConfig.properties";
		inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);
		
		if(inputStream!=null)
		{
			config.load(inputStream);
		}
		else
		{
			throw new FileNotFoundException(configFileName +" file not found");
		}
		this.ip=config.getProperty("ip");
		this.port=Integer.parseInt(config.getProperty("port"));
	}
	

}
