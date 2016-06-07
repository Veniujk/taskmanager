package pl.kielce.tu.kronos.client.threads;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.tableClasses.Task;

public class TaskEditThread implements Runnable{

	private Task task;
	private MainController main;
	private List<Integer> parents;

	public TaskEditThread(MainController main, Task task) {
		this.task = task;
		this.main = main;
	}
	
	public TaskEditThread(MainController main, Task task, List<Integer> parents) {
		this.task = task;
		this.main = main;
		this.parents = parents;
	}
	
	@Override
	public void run() {
		if(this.parents != null && this.parents.isEmpty()){
			this.task.setStatus("active");
		}else if(this.parents != null){
			this.task.setStatus("waiting");
		}
		if(this.task.getId() > 0){
			boolean isUpdated =  this.task.update();
			if(isUpdated && !this.parents.isEmpty()){
				this.task.setParents(this.parents);
			}
		}else{
			this.task.add();
			if(this.task.getId() > 0 && !this.parents.isEmpty()){
				this.task.setParents(this.parents);
			}
		}
		List<Task> taskList = Task.getTasks(this.task.getIdProject());
		Platform.runLater(() -> this.main.updateTasks(taskList));
	}
}
