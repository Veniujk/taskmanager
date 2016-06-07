package pl.kielce.tu.kronos.client.threads;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import pl.kielce.tu.kronos.client.AddTaskController;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.TableViewCreator;
import pl.kielce.tu.kronos.client.tableClasses.Task;
import javafx.collections.ObservableList;

public class TaskGetThread2 implements Runnable{
	
	private int idProject;
	private AddTaskController controller;

	public TaskGetThread2(AddTaskController controller, int idProject) {
		this.idProject = idProject;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		List<Task> taskList = Task.getTasks(this.idProject);
		ObservableList<Task> oList = FXCollections.observableArrayList(taskList);
		Platform.runLater(()->this.controller.getAllTeamTask(oList));
	}
}
