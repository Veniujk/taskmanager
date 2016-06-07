package pl.kielce.tu.kronos.model;

public class Project implements TreeViewAccess {
int idProject;
String name;
int idTeam;
	public Project() {
	}
	public int getIdProject() {
		return idProject;
	}
	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIdTeam() {
		return idTeam;
	}
	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}
	@Override
	public String toString()
	{
		return this.getName();
	}
	@Override
	public int getId() {
		return this.idProject;
	}

}
