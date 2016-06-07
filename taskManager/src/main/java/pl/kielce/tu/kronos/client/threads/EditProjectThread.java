package pl.kielce.tu.kronos.client.threads;

import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.TreeViewCreator;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import javafx.application.Platform;

public class EditProjectThread implements Runnable{
	String name;
	int idTeam;
	SettingsController controller;
	MainController main;
	int idProject;
	
	public EditProjectThread(SettingsController controller,MainController main, String name,int idTeam,int idProject) {
		this.name = name;
		this.idTeam = idTeam;
		this.controller = controller;
		this.main = main;
		this.idProject = idProject;
	}

	@Override
	public void run() {
		Project.editProject(this.name, this.idTeam, this.idProject);
		Platform.runLater(()-> new TreeViewCreator().generateTree(main));
		this.controller.enableButtonUpdateProject();
	}

}
