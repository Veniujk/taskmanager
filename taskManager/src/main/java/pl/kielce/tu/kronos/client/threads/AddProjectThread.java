package pl.kielce.tu.kronos.client.threads;

import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.TreeViewCreator;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import javafx.application.Platform;

public class AddProjectThread implements Runnable{

	String name;
	int idTeam;
	SettingsController controller;
	MainController main;
	
	public AddProjectThread(SettingsController controller,MainController main, String name,int idTeam) {
		this.name = name;
		this.idTeam = idTeam;
		this.controller = controller;
		this.main = main;
	}

	@Override
	public void run() {
		Project.addProject(this.name, this.idTeam);
		Platform.runLater(()-> new TreeViewCreator().generateTree(main));
		this.controller.enableButtonAddProject();
		
		
	}

}
