package pl.kielce.tu.kronos.client.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import pl.kielce.tu.kronos.model.ConstantFields;

/**
 * 
 * @author Bartosz Piœkiewicz
 * @author Dawid Kij
 * Class used to store login data, such as ip, port and login
 */
public class ConnectionProperties {
	private static final String FILENAME = "connectionConfig.properties";
	private String username;
	private String ip;
	private int port;

	/**
	 * Creates an instance of class with fields read from connectionConfig.properties file.
	 */
	public ConnectionProperties() {
		this.loadConfigFile();
	}
	
	/**
	 * Creates an instance of class and saves parameters into connectionConfig.properties file
	 * @param username String stored in connectionConfig.properties file.
	 * @param ip IP of the server.
	 * @param port Port on which server is listening on.
	 * @throws IOException when unable to create config file
	 */
	public ConnectionProperties(String username, String ip, int port) throws IOException {
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.saveConfigFile();
	}

	public String getUsername() {
		return username;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
/**
 * load ip,username,port from file
 */
	private void loadConfigFile(){
		Properties config = new Properties();
		try(InputStream inputStream = new FileInputStream(new File(System.getProperty("user.dir") + ConstantFields.PATH_SEPARATOR + FILENAME)) ){
			config.load(inputStream);
			this.ip = config.getProperty("ip");	
			this.port = Integer.parseInt(config.getProperty("port"));
			this.username = config.getProperty("username");
		}catch(IOException ex){
			ex.printStackTrace();
			this.ip = "";
			this.username = "";
			this.port = 0;
		}
	}
	/**
	 * save configuration to file
	 * @throws IOException
	 */
	private void saveConfigFile() throws IOException{
		String path = System.getProperty("user.dir")+ ConstantFields.PATH_SEPARATOR +FILENAME;
		try(FileOutputStream fos = new FileOutputStream(path)){
			this.setConfigVariables(fos);
		} catch (FileNotFoundException e) {
			try{
				this.createFile(path);
				FileOutputStream fos = new FileOutputStream(path);
				this.setConfigVariables(fos);
			}catch(IOException ex){
				throw new IOException(e);
			}
		}
	}
	/**
	 * create file
	 * @param path
	 * @throws IOException
	 */
	private void createFile(String path) throws IOException{
		File file = new File(path);
		file.createNewFile();
	}
	/**
	 * method save configuration as properties
	 * @param fos
	 * @throws IOException
	 */
	private void setConfigVariables(FileOutputStream fos) throws IOException{
		Properties config = new Properties();
		config.put("ip", this.ip);
		config.put("port", Integer.toString(this.port));
		config.put("username", this.username);
		config.store(fos,"edited");
	}
}
