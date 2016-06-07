package pl.kielce.tu.kronos.client.threads;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.TreeViewCreator;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import pl.kielce.tu.kronos.client.MainController;

public class DeleteProjectThread implements Runnable{
SettingsController controller;
int idProject;
MainController main;
	public DeleteProjectThread(SettingsController controller,MainController main,int idProject) {
		this.controller = controller;
		this.idProject = idProject;
		this.main= main;
	}

	@Override
	public void run() {
		Project.deleteProject(this.idProject);
		this.controller.enableButtonDeleteProject();
		Platform.runLater(()-> new TreeViewCreator().generateTree(main));
		this.controller.enableButtonDeleteProject();
		
	}

}
