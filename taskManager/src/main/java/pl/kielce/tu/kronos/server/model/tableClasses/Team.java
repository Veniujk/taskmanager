package pl.kielce.tu.kronos.server.model.tableClasses;

import org.json.JSONObject;

public class Team implements JSONAble{

	private int id;
	private String name;
	private int idTeamLeader;
	
	public Team(int id, String name, int idTeamLeader) {
		this.id = id;
		this.name = name;
		this.idTeamLeader = idTeamLeader;
	}
	
	public Team(String JSONData){
		JSONObject obj = new JSONObject(JSONData);
		this.id = obj.getInt("id");
		this.name = obj.isNull("name")? null : obj.getString("name");
		this.idTeamLeader = obj.isNull("idTeamLeader")?0:obj.getInt("idTeamLeader");
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getIdTeamLeader() {
		return idTeamLeader;
	}

	@Override
	public String JSONExport() {
		JSONObject obj = new JSONObject();
		obj.put("id", new Integer(this.id));
		obj.put("name", this.name);
		obj.put("idTeamLeader", new Integer(this.idTeamLeader));
		return obj.toString();
	}

	
}
