package pl.kielce.tu.kronos.server.model.tableManipulators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;


import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;
import pl.kielce.tu.kronos.server.model.tableClasses.Project;
import pl.kielce.tu.kronos.server.model.tableClasses.Team;

public class ProjectModel extends Model {

	public ProjectModel(Request request, Person user) {
		super(request, user);
	}

	@Override
	protected String doGetRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		PreparedStatement statement;
		if(this.JSONData == null){
			statement = this.getStatement("SELECT * FROM project");
		}else{
			statement = this.getStatement("SELECT * FROM project WHERE id_team = ?");
			statement.setInt(1, new Project(this.JSONData).getIdTeam());
		}
		ResultSet result = statement.executeQuery();
		List<String> teamList = new ArrayList<>(20);
		while(result.next()){
			int id = result.getInt("id_project");
			String name = result.getString("name");
			int idTeam = result.getInt("id_team");
			String ProjectJson = new Project(id, name, idTeam).JSONExport();
			teamList.add(ProjectJson);
		}
		JSONArray array = new JSONArray(teamList);
		return array.toString();
	}

	@Override
	protected String doPostRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		Project p = new Project(this.JSONData);
		PreparedStatement statement = this.getStatement("INSERT INTO project(name, id_team) VALUES(?, ?)", true);
		statement.setString(1, p.getName());
		statement.setInt(2, p.getIdTeam());
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("Invalid number of affected rows");
		}
		ResultSet generatedKeys = statement.getGeneratedKeys();
		generatedKeys.next();
		p.setId(generatedKeys.getInt(1));
		return p.JSONExport();
	}

	@Override
	protected String doPutRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		Project p = new Project(this.JSONData);
		PreparedStatement statement = this.getStatement("UPDATE project SET name=?, id_team=? WHERE id_project =?");
		statement.setString(1, p.getName());
		statement.setInt(2, p.getIdTeam());
		statement.setInt(3, p.getId());
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("Invalid number of affected rows");
		}
		return p.JSONExport();
	}

	@Override
	protected String doDeleteRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		Project p = new Project(this.JSONData);
		PreparedStatement statement =  this.getStatement("SELECT COUNT(id_project) as number FROM task WHERE id_project = ?");
		statement.setInt(1, p.getId());
		ResultSet result = statement.executeQuery();
		result.next();
		int numberOfTasks = result.getInt(1);
		if(numberOfTasks > 0){
			throw new SQLException("Project has tasks, delete denied");
		}
		int affectedRows = statement.executeUpdate("DELETE FROM project WHERE id_project = " + p.getId());
		if(affectedRows != 1){
			throw new SQLException("Invalid number of affected rows");
		}
		return null;
	}

}
