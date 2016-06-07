package pl.kielce.tu.kronos.model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

public class DatabaseTest {
	
	private static String jdbc_driver;
	private static String jdbc_driverConnection;
	private static String database_url;
	private static String database_user;
	private static String database_password;
	private InputStream inputStream;
	
	public static void main(String[] args)throws IOException {
		DatabaseTest db= new DatabaseTest();
		db.getConfigConnection();
		Connection connection = null;
		Statement statement = null;
		
		try{
			Class.forName(jdbc_driver).newInstance();
			//connection = DriverManager.getConnection("jdbc:mysql://78.46.43.228:3306/starysad_java?user=starysad_java&password=j@vaProj");
			connection=DriverManager.getConnection(jdbc_driverConnection+"://"+database_url+"?user="+database_user+"&password="+database_password);
		}catch(SQLException ex){
			System.err.println("Connection problem");
			ex.printStackTrace();
			return;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("gtfo");

		
		try{
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM task");
			while(result.next()){
				//System.out.printf("%d %s %s %s %b",result.getInt("id_person"), result.getString("username"), result.getString("password"), result.getString("name"), result.getBoolean("is_admin"));
				System.out.print(result.getInt(1));
				System.out.print(" ");
				System.out.print(result.getString(2));
				System.out.print(" ");
				System.out.print(result.getString(3));
				System.out.print(" ");
				System.out.print(result.getTimestamp(4));
				System.out.print(" ");
				System.out.println();
			}
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		
	
	}
	private void getConfigConnection() throws IOException
	{
	
		Properties config = new Properties();
		String configFileName="connectionDB.properties";
		inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);
		
		if(inputStream!=null)
		{
			config.load(inputStream);
		}
		else
		{
			throw new FileNotFoundException(configFileName +" file not found");
		}
		jdbc_driver=config.getProperty("jdbc_driver");
		database_url=config.getProperty("database_url");
		database_user=config.getProperty("database_user");
		database_password=config.getProperty("database_password");
		jdbc_driverConnection=config.getProperty("jdbc_driverConnection");
	}
}

