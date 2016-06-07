package pl.kielce.tu.kronos.client.threads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import pl.kielce.tu.kronos.client.EditTaskController;
import pl.kielce.tu.kronos.client.tableClasses.Task;

public class ParentGetThread implements Runnable{

	private ListView<Task> taskParents;
	private Task task;
	private ListView<Task> allTasks;
	
	public ParentGetThread(ListView<Task> taskParents, ListView<Task> allTasks, Task task) {
		this.taskParents = taskParents;
		this.task = task;
		this.allTasks = allTasks;
	}

	@Override
	public void run() {
		List<Task> tasks = Task.getTasks(this.task.getIdProject());
		List<Integer> parentList = this.task.getParents();
		List<Task> parents = new ArrayList<>();
		Iterator<Task> iterator = tasks.iterator();
		while(iterator.hasNext()){
			Task t = iterator.next();
			if(t.equals(this.task)){
				iterator.remove();
				continue;
			}
			for(Integer i: parentList){
				if(t.getId() == i){
					parents.add(t);
					iterator.remove();
					break;
				}
			}
		}
		Platform.runLater( () -> {
			this.allTasks.getItems().addAll(tasks);
			this.taskParents.getItems().addAll(parents);
		});
		
	}
}
