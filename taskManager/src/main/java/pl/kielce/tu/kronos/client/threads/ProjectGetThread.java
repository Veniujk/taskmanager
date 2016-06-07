package pl.kielce.tu.kronos.client.threads;

import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Project;

import java.lang.management.PlatformLoggingMXBean;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class ProjectGetThread implements Runnable{
SettingsController controller;
int idTeam;

public ProjectGetThread(SettingsController controller, int idTeam) {
	super();
	this.controller = controller;
	this.idTeam = idTeam;
}

@Override
public void run() {
	List<Project> list = Project.getProjects(this.idTeam);
	ObservableList<Project> oList = FXCollections.observableArrayList();
	oList.addAll(list);
	Platform.runLater(()->this.controller.projectChoiceBox.setItems(oList));
	Platform.runLater(()->this.controller.enableProjectChoiceBox());
	
	
}
	
}
