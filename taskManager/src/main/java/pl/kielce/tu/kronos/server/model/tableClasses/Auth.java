package pl.kielce.tu.kronos.server.model.tableClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import pl.kielce.tu.kronos.server.database.Database;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;

/**
 * 
 * @author Bartosz Piskiewicz
 *
 */
public class Auth{

	private String username;
	private String password;
	private Person person;
	
	public Auth(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.username = obj.getString("username");
		this.password = obj.getString("password");
	}
	
	public boolean isLogged(){
		this.person = getPersonFromDatabase();
		if(this.person != null){
			if(this.password.equals(this.person.getPassword())){
				return true;
			}
		}
		return false;
	}
	
	public Person getPerson(){
		return this.person;
	}
	
	private Person getPersonFromDatabase(){
		try(Database db = new Database()){
			PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM person WHERE USERNAME = ?");
			preparedStatement.setString(1, this.username);
			ResultSet result = preparedStatement.executeQuery();
			Person p = null;
			while(result.next()){
				int id = result.getInt("id_person");
				String username = result.getString("username");
				String password = result.getString("password");
				String name = result.getString("name");
				boolean isAdmin = result.getBoolean("is_admin");
				p = new Person(id, username, password, name, isAdmin);
			}
			return p;
		} catch (DatabaseCredencialsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
