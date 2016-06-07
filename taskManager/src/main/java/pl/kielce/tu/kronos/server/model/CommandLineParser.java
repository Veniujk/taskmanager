package pl.kielce.tu.kronos.server.model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.server.database.Database;

public class CommandLineParser {
	
	public CommandLineParser(String[] args) throws IOException {
		if(!args[0].startsWith("-") || args.length > 4){
			throw new IllegalArgumentException(args[0]);
		}
		if(args.length != args[0].length()){
			throw new IllegalArgumentException();
		}
		
		int portIndex = args[0].indexOf("p");
		int adminPasswordIndex = args[0].indexOf("a");
		int databaseCredencialsIndex = args[0].indexOf("d");
		if(databaseCredencialsIndex != -1){
			String[] param = args[databaseCredencialsIndex].split("::");
			String ip = param[0];
			int port = Integer.parseInt(param[1]);
			String user = param[2];
			String password = param[3];
			String database = param[4];
			String jdbcDriver = param[5];
			String jdbcConnection = param[6];
			new DatabaseProperties(ip, port, user, password, database, jdbcDriver, jdbcConnection);
		}
		if(portIndex != -1){
			int port = Integer.parseInt(args[portIndex]);
			new ServerProperties(port);
		}
		if(adminPasswordIndex != -1){
			String adminPassword =  args[adminPasswordIndex];
			//create admin user with given pass
		}
	}
	
	private void updateAdminPassword(String password) throws Exception{
		Database db = new Database();
		ResultSet result = db.query("SELECT COUNT(id_person) FROM person WHERE username = admin");
		result.next();
		PreparedStatement statement;
		if(result.getInt(1) == 0){
			statement = db.prepareStatement("INSERT INTO person(username, password) VALUES('admin', ?)");
			statement.setString(1, password);
		}else{
			statement = db.prepareStatement("UPDATE person SET password = ? WHERE username = 'admin'");
			statement.setString(1, password);
		}
		statement.executeUpdate();
		db.close();
	}
	
}