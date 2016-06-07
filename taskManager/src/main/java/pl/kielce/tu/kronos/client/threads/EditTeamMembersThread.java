package pl.kielce.tu.kronos.client.threads;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Team;

public class EditTeamMembersThread implements Runnable{

	private int idTeam;
	private int idPerson;
	private String method;
	private SettingsController controller;
	
	public EditTeamMembersThread(SettingsController controller, String method, int idTeam, int idPerson) {
		this.method = method;
		this.idTeam = idTeam;
		this.idPerson = idPerson;
		this.controller = controller;
	}

	@Override
	public void run() {
		if(this.method.equals("Add")){
			Team.addTeamMember(this.idTeam, this.idPerson);
			Platform.runLater(() -> this.controller.updatePersonList(true));
		}else{
			Team.removeTeamMember(this.idTeam, this.idPerson);
			Platform.runLater(() -> this.controller.updatePersonList(false));
		}
		
	}
}
