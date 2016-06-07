package pl.kielce.tu.kronos.server.model.tableManipulators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONException;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;
import pl.kielce.tu.kronos.server.database.Database;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.tableClasses.Person;

public abstract class Model{
	
	private Database db;
	
	protected Person user;
	protected String method;
	protected String JSONData;
	
	
	public Model(Request request, Person user) {
		this.method = request.getMethod();
		this.JSONData = request.getJSONdata();
		this.user = user;
	}
	
	public Response serveRequest(){
		try{
			switch(this.method){
			case "GET":
				Response r = new Response((short)200, this.doGetRequest());
				this.db.close();
				return r;
			case "POST":
				Response r1 = new Response((short) 200, this.doPostRequest());
				this.db.close();
				return r1; 
			case "PUT":
				Response r2 = new Response((short) 200, this.doPutRequest());
				this.db.close();
				return r2;
			case "DELETE":
				Response r3 = new Response((short) 200, this.doDeleteRequest());
				this.db.close();
				return r3;
			case "HEAD":
				Response r4 = new Response(this.doHeadRequest());
				return r4;
			default:
				this.db.close();
				return new Response((short)400, "Invalid method"); 
			}
		}catch(SQLException e){
			e.printStackTrace();
			return new Response((short) 501, e.getMessage());
		}catch(ClassNotFoundException | DatabaseCredencialsException e){
			e.printStackTrace();
			return new Response((short) 400, e.getMessage());
		}catch(IllegalAccessException e){
			return new Response((short) 403, e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return new Response((short) 500, e.getMessage());
		}
	}
	
	protected PreparedStatement getStatement(String query) throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		this.db = new Database();
		return this.db.prepareStatement(query);
	}
	
	protected PreparedStatement getStatement(String query, boolean isGeneratedKeyReturned) throws DatabaseCredencialsException, ClassNotFoundException, SQLException{
		this.db = new Database();
		return this.db.prepareStatement(query, isGeneratedKeyReturned);
	}
	
	protected abstract String doGetRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException;
	
	protected abstract String doPostRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, IllegalAccessException, JSONException, ParseException;
	
	protected abstract String doPutRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException, IllegalAccessException;
	
	protected abstract String doDeleteRequest() throws DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException, IllegalAccessException;
	
	protected short doHeadRequest() throws IllegalAccessException, DatabaseCredencialsException, ClassNotFoundException, SQLException, JSONException, ParseException {
		throw new IllegalAccessException("Method not supported");
	}
}
