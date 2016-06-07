package pl.kielce.tu.kronos.server.model.tableManipulators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.Server;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.RequestThread;
import pl.kielce.tu.kronos.server.model.tableClasses.Message;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;

public class PersonModel extends Model {
	
	public PersonModel(Request request, Person user) {
		super(request, user);
	}

	@Override
	protected String doGetRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		JSONObject requestData = new JSONObject(this.JSONData);
		PreparedStatement statement;
		if(!requestData.isNull("idProject")){
			statement = this.getStatement("SELECT person.id_person, username, person.name, is_admin "
					+ "FROM person, person_team, team, project WHERE person.id_person = person_team.id_person "
					+ "AND person_team.id_team = team.id_team and team.id_team = project.id_team and project.id_project = ?");
			statement.setInt(1, requestData.getInt("idProject")); 
		}else if(!requestData.isNull("idTeam")){
			statement = this.getStatement("SELECT person.id_person, username, person.name, is_admin "
					+ "FROM person, person_team WHERE person.id_person = person_team.id_person "
					+ "AND person_team.id_team = ?");
			statement.setInt(1, requestData.getInt("idTeam")); 
		}else{
			statement = this.getStatement("SELECT id_person, username, name, is_admin FROM person");
		}
		ResultSet result = statement.executeQuery();
		List<Person> messageList = new ArrayList<>(30);
		while(result.next()){
			int id = result.getInt("id_person");
			String username = result.getString("username");
			String name = result.getString("name");
			boolean isAdmin = result.getBoolean("is_admin");
			Person p = new Person(id, username, "", name, isAdmin);
			messageList.add(p);
		}
		JSONArray array = new JSONArray(messageList);
		return array.toString();
	}

	@Override
	protected String doPostRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException {
		if(!this.user.isAdmin()){
			throw new IllegalAccessException("You are not an admin");
		}
		Person p = new Person(this.JSONData);
		PreparedStatement statement = this.getStatement("INSERT INTO person(username, password, name, is_admin) VALUES(?, ?, ?, ?)", true);
		statement.setString(1, p.getUsername());
		statement.setString(2, p.getPassword());
		statement.setString(3, p.getName());
		statement.setBoolean(4, p.isAdmin());
		int affectedRows = statement.executeUpdate();
		if(affectedRows == 0){
			throw new SQLException("Insert query failed.");
		}
		try(ResultSet generatedKeys = statement.getGeneratedKeys()){
			if(generatedKeys.next()){
				p.setId(generatedKeys.getInt(1));
			}else{
				throw new SQLException("Insert query failed. No id detected.");
			}
		}
		return p.JSONExport();
	}

	@Override
	protected String doPutRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException {
		Person p = new Person(this.JSONData);
		if(!this.user.isAdmin() && (p.getId() != this.user.getId())){
			throw new IllegalAccessException("you are not an admin");
		}
		PreparedStatement statement;
		if(0 != p.getId()){
			this.logoutUser(p.getId());
			statement = this.getStatement("UPDATE person SET name = ?, password =?, is_admin = ? WHERE id_person = ?",true);
			statement.setString(1, p.getName());
			statement.setString(2, p.getPassword());			
			statement.setBoolean(3, p.isAdmin());
			statement.setInt(4, p.getId());
		}else if(p.getPassword().trim().equals("")){
			statement = this.getStatement("UPDATE person SET name = ? WHERE id_person = ?", true);
			statement.setString(1, p.getName());
			statement.setInt(2, this.user.getId());
		}else{
			statement = this.getStatement("UPDATE person SET name = ?, password =? WHERE id_person = ?", true);
			statement.setString(1, p.getName());
			statement.setString(2, p.getPassword());
			statement.setInt(3, this.user.getId());
		}
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("Invalid affected rows number");
		}
		return p.JSONExport();
	}

	@Override
	protected String doDeleteRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException {
		if(!this.user.isAdmin()){
			throw new IllegalAccessException("you are not an admin");
		}
		Person p = new Person(this.JSONData);
		this.logoutUser(p.getId());
		PreparedStatement statement = this.getStatement("DELETE FROM person_team WHERE id_person = "+ p.getId());
		statement.executeUpdate();
		statement.executeUpdate("DELETE FROM logs WHERE id_person = "+ p.getId());
		statement.executeUpdate("DELETE FROM message WHERE id_person = "+ p.getId());
		statement.executeUpdate("UPDATE team SET id_team_leader = null WHERE id_team_leader = "+ p.getId());
		statement.executeUpdate("UPDATE task SET id_person = null WHERE id_person = "+ p.getId());
		int affectedRows = statement.executeUpdate("DELETE FROM person WHERE id_person = " + p.getId());
		if(affectedRows != 1){
			throw new SQLException("invalid affected rows number");
		}
		return null;
	}

	private void logoutUser(int idUser){
		for(RequestThread r: Server.threadList){
			if(r.checkUserId(idUser)){
				r.logout();
				break;
			}
		}
	}
	
}
