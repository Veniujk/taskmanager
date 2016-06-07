package pl.kielce.tu.kronos.server.model.tableClasses;

import org.json.JSONObject;

public class Person implements JSONAble{

	private int id;
	private String username;
	private String password;
	private String name;
	private boolean isAdmin;
	
	public Person(int id, String username, String password, String name, boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.isAdmin = isAdmin;
	}
	
	public Person(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.username = obj.isNull("username")? "" : obj.getString("username");
		this.password = obj.getString("password");
		this.name = obj.getString("name");
		this.isAdmin = obj.getBoolean("admin");
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("username", this.username);
		obj.put("name", this.name);
		obj.put("admin", new Boolean(this.isAdmin));
		return obj.toString();
	}

	

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	
	
	
	

	
}
