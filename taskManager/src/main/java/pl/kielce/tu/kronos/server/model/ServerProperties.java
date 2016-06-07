package pl.kielce.tu.kronos.server.model;

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
 * Class used to store login data, such as ip, port and login
 */
public class ServerProperties {
	private static final String FILENAME = "serverConfig.properties";
	private int port;

	/**
	 * Creates an instance of class with fields read from connectionConfig.properties file.
	 */
	public ServerProperties() {
		this.loadConfigFile();
	}
	
	/**
	 * Creates an instance of class and saves parameters into connectionConfig.properties file
	 * @param port Port on which server is listening on.
	 * @throws IOException when unable to create config file
	 */
	public ServerProperties(int port) throws IOException {
		this.port = port;
		this.saveConfigFile();
	}

	public int getPort() {
		return port;
	}

	private void loadConfigFile(){
		Properties config = new Properties();
		try(InputStream inputStream = new FileInputStream(new File( System.getProperty("user.dir") + ConstantFields.PATH_SEPARATOR + FILENAME ))){
			config.load(inputStream);
			this.port = Integer.parseInt(config.getProperty("port"));
		}catch(IOException ex){
			this.port = 0;
		}
	}
	
	private void saveConfigFile() throws IOException{
		String path = System.getProperty("user.dir")+ ConstantFields.PATH_SEPARATOR + FILENAME;
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
	
	private void createFile(String path) throws IOException{
		File file = new File(path);
		file.createNewFile();
	}
	
	private void setConfigVariables(FileOutputStream fos) throws IOException{
		Properties config = new Properties();
		config.put("port", Integer.toString(this.port));
		config.store(fos,"edited");
	}
}
