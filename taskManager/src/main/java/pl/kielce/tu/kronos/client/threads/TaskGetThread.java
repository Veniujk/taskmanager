package pl.kielce.tu.kronos.client.threads;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.TableView;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.TableViewCreator;
import pl.kielce.tu.kronos.client.tableClasses.Task;

public class TaskGetThread implements Runnable{
	
	private int idProject;
	private MainController main;

	public TaskGetThread(MainController main, int idProject) {
		this.idProject = idProject;
		this.main = main;
	}
	
	@Override
	public void run() {
		List<Task> taskList = Task.getTasks(this.idProject);
		Platform.runLater(() -> this.main.updateTasks(taskList));
	}
}
