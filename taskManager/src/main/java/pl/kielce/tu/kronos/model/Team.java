package pl.kielce.tu.kronos.model;

public class Team implements TreeViewAccess{
private int idTeam;
private String name;
private int idTeamLeader;

	public int getIdTeam() {
	return idTeam;
}

public void setIdTeam(int idTeam) {
	this.idTeam = idTeam;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public int getIdTeamLeader() {
	return idTeamLeader;
}

public void setIdTeamLeader(int idTeamLeader) {
	this.idTeamLeader = idTeamLeader;
}
@Override
public String toString()
{
	return this.getName();
}
	public Team() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getId() {
		return this.idTeam;
	}

}
