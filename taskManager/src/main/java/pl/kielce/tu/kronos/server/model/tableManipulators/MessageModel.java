package pl.kielce.tu.kronos.server.model.tableManipulators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.Server;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.tableClasses.Message;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;
import pl.kielce.tu.kronos.server.model.tableClasses.Task;

public class MessageModel extends Model {

	public MessageModel(Request request, Person user) {
		super(request, user);
	}

	@Override
	protected String doGetRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		PreparedStatement statement = this.getStatement("SELECT id_message, message.id_person, created_at, id_task, content, person.name FROM message, person WHERE person.id_person = message.id_person AND id_task = ? ORDER BY created_at");
		int idTask = new Message(this.JSONData).getIdTask();
		statement.setInt(1, idTask);
		ResultSet result = statement.executeQuery();
		List<Message> messageList = new ArrayList<>(30);
		while(result.next()){
			int id = result.getInt("id_message");
			String content = result.getString("content");
			Date createdAt = (Date)result.getObject("created_at");
			int idPerson = result.getInt("id_person");
			idTask = result.getInt("id_task");
			String user = result.getString("person.name");
			Message m = new Message(id, content, idPerson, idTask, createdAt, user);
			messageList.add(m);
		}
		JSONArray array = new JSONArray(messageList);
		return array.toString();
	}

	@Override
	protected String doPostRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		PreparedStatement statement = this.getStatement("INSERT INTO message(id_person, created_at, id_task, content) VALUES(?, current_timestamp, ?, ?)", true);
		Message m = new Message(this.JSONData);
//		m.setUser(this.user.getName());
		statement.setInt(1, this.user.getId());
		statement.setInt(2, m.getIdTask());
		statement.setString(3, m.getContent());
		int affectedRows = statement.executeUpdate();
		if(affectedRows == 0){
			throw new SQLException("Insert query failed.");
		}
		try(ResultSet generatedKeys = statement.getGeneratedKeys()){
			if(generatedKeys.next()){
				m.setId(generatedKeys.getInt(1));
			}else{
				throw new SQLException("Insert query failed. No id detected.");
			}
		}
		Server.recorder.addMessageChanges(m.getIdTask());
		return m.JSONExport();
	}

	@Override
	protected String doPutRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		throw new SQLException("Invalid method");
	}

	@Override
	protected String doDeleteRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException {
		throw new SQLException("Invalid method");
	}

	@Override
	protected short doHeadRequest() throws IllegalAccessException, DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		Message m = new Message(this.JSONData);
		if(Server.recorder.isMessageChanged(m.getIdTask(), m.getCreatedAt())){
			return (short) 302;
		}else{
			return (short) 304;
		}
	}
}
