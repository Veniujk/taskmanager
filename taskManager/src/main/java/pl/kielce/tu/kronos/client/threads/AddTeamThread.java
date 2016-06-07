package pl.kielce.tu.kronos.client.threads;

import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import javafx.application.Platform;
import pl.kielce.tu.kronos.client.threads.TreeViewGeneratorThread;

public class AddTeamThread implements Runnable{
SettingsController controller;
MainController main;
String name;
int idTeamLeader;
public AddTeamThread(SettingsController controller,MainController main ,String name, int idTeamLeader) {
	super();
	this.controller = controller;
	this.name = name;
	this.idTeamLeader = idTeamLeader;
	this.main = main;
	
}
@Override
public void run() {
	Team.addTeam(this.idTeamLeader, this.name);
	Platform.runLater(()->controller.enableButtonAddTeam());
	Platform.runLater(()->new TreeViewGeneratorThread(main).run());
	Platform.runLater(()->this.controller.teamChoiceBoxesRefresh());
	
	
}
	

}
