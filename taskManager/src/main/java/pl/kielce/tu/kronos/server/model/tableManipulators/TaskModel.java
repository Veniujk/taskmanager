package pl.kielce.tu.kronos.server.model.tableManipulators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.Server;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;
import pl.kielce.tu.kronos.server.model.tableClasses.Task;

public class TaskModel extends Model {

	public TaskModel(Request request, Person user) {
		super(request, user);
	}

	@Override
	protected String doGetRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		JSONObject obj = new JSONObject(this.JSONData);
		if(obj.isNull("childOf")){
			return this.getTasks();
		}else{
			return this.getParents(obj.getInt("childOf"));
		}
	}

	@Override
	protected String doPostRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		JSONObject obj = new JSONObject(this.JSONData);
		if(!obj.isNull("idProject")){
			Server.recorder.addTaskChanges(obj.getInt("idProject"));
		}
		if(obj.isNull("childOf")){
			return this.addTask();
		}else{
			System.out.println("i am setting parents of task");
			return this.setParents();
		}
	}

	@Override
	protected String doPutRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		PreparedStatement statement = this.getStatement("UPDATE task SET name=?, status=?, updated_at=current_timestamp, deadline=?, "
				+ "id_person=?, id_project=? WHERE id_task=?");
		Task t = new Task(this.JSONData);
		statement.setString(1, t.getName());
		statement.setString(2, t.getStatus());
		statement.setDate(3,  new java.sql.Date(t.getDeadline().getTime()));
		statement.setInt(4, t.getIdPerson());
		statement.setInt(5, t.getIdProject());
		statement.setInt(6, t.getId());
		int affectedRows = statement.executeUpdate();
		if(affectedRows != 1){
			throw new SQLException("Wrong number of affected rows " + affectedRows);
		}
		if(t.getStatus().equals("done")){
			System.out.println("status changed to done");
			this.updateChildrenStatus(t.getId());
		}
		Server.recorder.addTaskChanges(t.getIdProject());
		return t.JSONExport();
	}

	@Override
	protected String doDeleteRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		Task t = new Task(this.JSONData);
		PreparedStatement statement = this.getStatement("Delete from message WHERE id_task = " + t.getId());
		statement.executeUpdate();
		statement.executeUpdate("delete from logs where id_task = " + t.getId());
		statement.executeUpdate("delete from task_parent where id_task = " + t.getId() +" or id_parent = "+t.getId());
		int affectedRows = statement.executeUpdate("DELETE FROM task where id_task = "+t.getId());
		if(affectedRows != 1){
			throw new SQLException("Wrong number of affected rows: " + affectedRows);
		}
		Server.recorder.addTaskChanges(t.getIdProject());
		return null;
	}
	
	@Override
	protected short doHeadRequest() throws IllegalAccessException, DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		Task t = new Task(this.JSONData);
		if(Server.recorder.isTaskChanged(t.getIdProject(), t.getUpdatedAt())){
			return (short) 302;
		}else{
			return (short) 304;
		}
	}

	private String getTasks() throws SQLException, DatabaseCredencialsException, ClassNotFoundException{
		PreparedStatement statement = this.getStatement("SELECT id_task, task.name, status, created_at, updated_at, deadline, id_project, task.id_person, person.name FROM task left outer join person on person.id_person = task.id_person where status <> 'done' and id_project = ?");
		int idProject;
		try {
			idProject = new Task(this.JSONData).getIdProject();
			statement.setInt(1, idProject);
			ResultSet result = statement.executeQuery();
			List<Task> taskList = new ArrayList<>(30);
			while(result.next()){
				int id = result.getInt("id_task");
				String name = result.getString("name");
				String status = result.getString("status");
				Date createdAt = (Date)result.getObject("created_at");
				Date updatedAt = (Date)result.getObject("updated_at");
				Date deadline = (Date)result.getObject("deadline");
				int idPerson = result.getInt("id_person");
				idProject = result.getInt("id_project");
				String userName = result.getString("person.name");
				Task t = new Task(id, name, status, createdAt, updatedAt, deadline, idPerson, idProject, userName);
				taskList.add(t);
			}
			JSONArray array = new JSONArray(taskList);
			return array.toString();
		}catch (ParseException e) {
			e.printStackTrace();
			JSONException ex = new JSONException("invalid date format");
			ex.initCause(e);
			throw ex;
		}
	}
	
	private String addTask() throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		PreparedStatement statement = this.getStatement("INSERT INTO task(name, status, created_at, updated_at, deadline, id_person, id_project) VALUES(?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?)", true);
		Task t;
		try {
			t = new Task(this.JSONData);
			statement.setString(1, t.getName());
			statement.setString(2, t.getStatus());
			statement.setDate(3,  new java.sql.Date(t.getDeadline().getTime()));
			statement.setInt(4, t.getIdPerson());
			statement.setInt(5, t.getIdProject());
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
			return t.JSONExport();
		} catch (ParseException e) {
			e.printStackTrace();
			JSONException ex = new JSONException("invalid date format");
			ex.initCause(e);
			throw ex;
		}
	}
	
	private String getParents(int id) throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		PreparedStatement statement = this.getStatement("SELECT id_parent FROM task_parent WHERE id_task = ?");
		statement.setInt(1, id);
		ResultSet result = statement.executeQuery();
		List<Integer> list = new LinkedList<>();
		while(result.next()){
			list.add(result.getInt("id_parent"));
		}
		JSONArray array = new JSONArray(list);
		return array.toString();
	}
	
	private String setParents() throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		System.out.println(this.JSONData);
		JSONObject obj = new JSONObject(this.JSONData);
		int idTask = obj.getInt("id");
		JSONArray array = obj.getJSONArray("childOf");
		Iterator<Object> iterator = array.iterator();
		PreparedStatement statement = this.getStatement("DELETE FROM task_parent WHERE id_task = ?");
		statement.setInt(1, idTask);
		statement.executeUpdate();
		while(iterator.hasNext()){
			statement.executeUpdate("INSERT INTO task_parent VALUES("+idTask+", "+((Integer)iterator.next()).intValue()+")");
		}
		statement.executeUpdate("UPDATE task SET status = 'waiting' WHERE id_task = " + idTask);
		return "success";
	}
	
	private void updateChildrenStatus(int idParent) throws SQLException, DatabaseCredencialsException, ClassNotFoundException{
		PreparedStatement statement = this.getStatement("SELECT id_task from task_parent WHERE id_parent = ?");
		statement.setInt(1, idParent);
		ResultSet result = statement.executeQuery();
		while(result.next()){
			this.updateChildStatus(result.getInt("id_task"));
		}
	}
	
	private void updateChildStatus(int child) throws JSONException, DatabaseCredencialsException, ClassNotFoundException, SQLException{
		PreparedStatement statement = this.getStatement("SELECT * FROM task, task_parent WHERE task_parent.id_parent = task.id_task AND task_parent.id_task = ? AND task.status <> 'done' ");
		statement.setInt(1, child);
		ResultSet result = statement.executeQuery();
		if(!result.isBeforeFirst()){
			statement.executeUpdate("UPDATE task SET status = 'active' WHERE id_task = " + child);
		}
	}
}
