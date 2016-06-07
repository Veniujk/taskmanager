package pl.kielce.tu.kronos.server.model.tableManipulators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;
import pl.kielce.tu.kronos.server.model.tableClasses.Task;
import pl.kielce.tu.kronos.server.model.tableClasses.Team;

public class TeamModel extends Model {

	public TeamModel(Request request, Person user) {
		super(request, user);
	}

	@Override
	protected String doGetRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		PreparedStatement statement;
		if(this.user.isAdmin()){
			statement = this.getStatement("SELECT team.id_team, name, id_team_leader FROM team");
		}else{
			statement = this.getStatement("SELECT team.id_team, name, id_team_leader FROM team, person_team WHERE team.id_team = person_team.id_team AND id_person = ?");
			statement.setInt(1, this.user.getId());
		}
		ResultSet result = statement.executeQuery();
		List<String> teamList = new ArrayList<>(20);
		while(result.next()){
			int id = result.getInt("id_team");
			String name = result.getString("name");
			int idTeamLeader = result.getInt("id_team_leader");
			String teamJson = new Team(id, name, idTeamLeader).JSONExport();
			teamList.add(teamJson);
		}
		JSONArray array = new JSONArray(teamList);
		return array.toString();
	}

	@Override
	protected String doPostRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException {
		JSONObject obj = new JSONObject(this.JSONData);
		if(obj.isNull("member")){
			return this.addTeam();
		}else{
			return this.addTeamMember();
		}
	}

	@Override
	protected String doPutRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException {
		if(!this.user.isAdmin()){
			throw new IllegalAccessException("You are not an admin");
		}
		Team t = new Team(this.JSONData);
		PreparedStatement statement = this.getStatement("UPDATE team SET name=?, id_team_leader=? WHERE id_team= ?");
		statement.setString(1, t.getName());
		statement.setInt(2, t.getIdTeamLeader());
		statement.setInt(3, t.getId());
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("Wrong number of affected rows " + affectedRows);
		}
		return t.JSONExport();
	}

	@Override
	protected String doDeleteRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException {
		JSONObject obj = new JSONObject(this.JSONData);
		if(obj.isNull("member")){
			return this.deleteTeam();
		}else{
			return this.removeTeamMember();
		}
		
	}
	
	private boolean isTeamEmpty(Team t) throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		PreparedStatement statement = this.getStatement("SELECT * FROM project WHERE id_team = ?");
		statement.setInt(1, t.getId());
		ResultSet result = statement.executeQuery();
		if(result.next()){
			return false;
		}
		return true;
	}

	private String addTeamMember() throws JSONException, SQLException, DatabaseCredencialsException, ClassNotFoundException{
		JSONObject obj = new JSONObject(this.JSONData);
		PreparedStatement statement = this.getStatement("INSERT INTO person_team VALUES(?, ?)");
		statement.setInt(1, obj.getInt("id_person"));
		statement.setInt(2, obj.getInt("id"));
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("invalid affected rows number");
		}
		return null;
	}
	
	private String removeTeamMember() throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		JSONObject obj = new JSONObject(this.JSONData);
		PreparedStatement statement = this.getStatement("DELETE FROM person_team WHERE id_team = ? AND id_person = ?");
		statement.setInt(1, obj.getInt("id"));
		statement.setInt(2, obj.getInt("id_person"));
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("invalid affected rows number");
		}
		return null;
	}
	
	private String addTeam() throws IllegalAccessException, DatabaseCredencialsException, ClassNotFoundException, SQLException{
		if(!this.user.isAdmin()){
			throw new IllegalAccessException("You are not an admin");
		}
		Team t = new Team(this.JSONData);
		PreparedStatement statement = this.getStatement("INSERT INTO team(name, id_team_leader) VALUES(?, ?)", true);
		statement.setString(1, t.getName());
		if(t.getIdTeamLeader() == 0){
			statement.setNull(2, java.sql.Types.INTEGER);
		}else{
			statement.setInt(2, t.getIdTeamLeader());
		}
		int affectedRows = statement.executeUpdate();
		if(affectedRows == 0){
			throw new SQLException("Insert query failed.");
		}
		try(ResultSet generatedKeys = statement.getGeneratedKeys()){
			if(generatedKeys.next()){
				t.setId(generatedKeys.getInt(1));
			}else{
				throw new SQLException("Insert query failed. No id detected.");
			}
		}
		this.JSONData = "{id_person: "+ t.getIdTeamLeader() +", id: "+ t.getId() +"}";
		this.addTeamMember();
		return t.JSONExport();
	}
	
	private String deleteTeam() throws SQLException, DatabaseCredencialsException, ClassNotFoundException, IllegalAccessException{
		if(!this.user.isAdmin()){
			throw new IllegalAccessException("You are not an admin");
		}
		Team t = new Team(this.JSONData);
		if(!this.isTeamEmpty(t)){
			throw new SQLException("Team has still projects");
		}
		PreparedStatement statement = this.getStatement("DELETE FROM person_team WHERE id_team = ?", true);
		statement.setInt(1, t.getId());
		statement.executeUpdate();
		int affectedRows = statement.executeUpdate("DELETE FROM team WHERE id_team = " + t.getId());
		if(affectedRows == 0){
			throw new SQLException("Invalid affected rows number");
		}
		return null;
	}
}
