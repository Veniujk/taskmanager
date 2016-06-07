package pl.kielce.tu.kronos.server.model.tableClasses;

import org.json.JSONObject;

public class Project implements JSONAble{
	
	private int id;
	private String name;
	private int idTeam;
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getIdTeam() {
		return idTeam;
	}

	public Project(int id, String name, int idTeam) {
		super();
		this.id = id;
		this.name = name;
		this.idTeam = idTeam;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Project(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.name = obj.getString("name");
		this.idTeam = obj.getInt("idTeam");
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("name", this.name);
		obj.put("idTeam", new Integer(this.idTeam));
		return obj.toString();
	}

	
	

}
