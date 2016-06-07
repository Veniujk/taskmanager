package pl.kielce.tu.kronos.server.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.DatabaseProperties;

/**
 * 
 * @author Bartosz Piskiewicz
 *
 */
public class Database implements AutoCloseable{
	
	private java.sql.Connection db;
	private java.sql.Statement statement;
	
	public PreparedStatement prepareStatement(String query) throws SQLException{
		return this.db.prepareStatement(query);
	}
	
	public PreparedStatement prepareStatement(String query, boolean isGeneratedKeyReturned) throws SQLException{
		if(isGeneratedKeyReturned){
			return this.db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		}
		return this.prepareStatement(query);
	}

	public Database() throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		DatabaseProperties properties = new DatabaseProperties();
		Class.forName(properties.getJdbcDriver());
		String url = properties.getJdbcConnection() + "://" + properties.getIp()+":"+properties.getPort()+"/"+properties.getDatabase();
		this.db = DriverManager.getConnection(url, properties.getUsername(), properties.getPassword());
		this.statement = db.createStatement();
	}
	
	public ResultSet query(String query) throws SQLException{
		return this.statement.executeQuery(query);
	}

	@Override
	public void close() throws Exception {
		this.db.close();
	}
}
