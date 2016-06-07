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
 * @author Dawid Kij
 * 
 * Class used to store login data, such as ip, port and login
 */
public class DatabaseProperties {
	private static final String FILENAME = "databaseConfig.properties";
	private String ip;
	private int port;
	private String username;
	private String password;
	private String database;
	private String jdbcDriver;
	private String jdbcConnection;

	/**
	 * Creates an instance of class with fields read from databaseConfig.properties file.
	 * @throws DatabaseCredencialsException 
	 */
	public DatabaseProperties() throws DatabaseCredencialsException {
		this.loadConfigFile();
	}
	
	/**
	 * Updates config file
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param database
	 * @throws IOException
	 */
	public DatabaseProperties(String ip, int port, String username, String password, String database, String jdbcDriver, String jdbcConnection) throws IOException {
		this.port = port;
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.database = database;
		this.jdbcConnection = jdbcConnection;
		this.jdbcDriver = jdbcDriver;
		this.saveConfigFile();
	}

	public int getPort() {
		return port;
	}

	public String getIp() {
		return ip;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDatabase() {
		return database;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getJdbcConnection() {
		return jdbcConnection;
	}
/**
 * method load database config from file
 * @throws DatabaseCredencialsException
 */
	private void loadConfigFile() throws DatabaseCredencialsException{
		Properties config = new Properties();
		try{
			config.load(new FileInputStream(new File(System.getProperty("user.dir") + ConstantFields.PATH_SEPARATOR + FILENAME )));
			this.port = Integer.parseInt(config.getProperty("port"));
			this.ip = config.getProperty("ip");
			this.username = config.getProperty("username");
			this.password = config.getProperty("password");
			this.database = config.getProperty("database");
			this.jdbcDriver = config.getProperty("jdbcDriver");
			this.jdbcConnection = config.getProperty("jdbcConnection").replace("\\", "");
		}catch(IOException ex){
			throw new DatabaseCredencialsException();
		}
	}
	/**
	 * method save database configuration to file
	 * @throws IOException
	 */
	private void saveConfigFile() throws IOException{
		String path = FILENAME;
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
	 * method creates file
	 * @param path
	 * @throws IOException
	 */
	private void createFile(String path) throws IOException{
		File file = new File(System.getProperty("user.dir") +ConstantFields.PATH_SEPARATOR+ path);
		file.createNewFile();
	}
	/**
	 * method sets properties
	 * @param fos
	 * @throws IOException
	 */
	private void setConfigVariables(FileOutputStream fos) throws IOException{
		Properties config = new Properties();
		config.put("port", Integer.toString(this.port));
		config.put("ip", this.ip);
		config.put("username", this.username);
		config.put("password", this.password);
		config.put("database", this.database);
		config.put("jdbcConnection", this.jdbcConnection);
		config.put("jdbcDriver", this.jdbcDriver);
		config.store(fos,"edited");
	}
}
