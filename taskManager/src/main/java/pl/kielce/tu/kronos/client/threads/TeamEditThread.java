package pl.kielce.tu.kronos.client.threads;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import pl.kielce.tu.kronos.client.TreeViewCreator;

public class TeamEditThread implements Runnable{
SettingsController controler;
MainController main;
int idTeam;
String name;
int idTeamLeader;
public TeamEditThread(SettingsController controler, MainController main, int idTeam, String name, int idTeamLeader) {
	super();
	this.controler = controler;
	this.main = main;
	this.idTeam = idTeam;
	this.name = name;
	this.idTeamLeader = idTeamLeader;
}
@Override
public void run() {
	new Team(this.idTeam, this.name, this.idTeamLeader).update();
	Platform.runLater(()->new TreeViewCreator().generateTree(main));
	Platform.runLater(()->this.controler.enableButtonUpdateTeam());
	Platform.runLater(()->this.controler.teamChoiceBoxesRefresh());
}
	
}
