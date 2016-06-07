package pl.kielce.tu.kronos.client.threads;

import java.util.List;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.tableClasses.CRUDAble;
import pl.kielce.tu.kronos.client.tableClasses.Person;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import pl.kielce.tu.kronos.client.tableClasses.Task;
import pl.kielce.tu.kronos.client.tableClasses.Team;

public class DataDeleteThread implements Runnable{

	private MainController main;
	private CRUDAble data;
	
	public DataDeleteThread(MainController main, CRUDAble data) {
		super();
		this.main = main;
		this.data = data;
	}

	@Override
	public void run() {
		this.data.remove();
		if(this.data instanceof Task){
			List<Task> taskList = Task.getTasks( ((Task)this.data).getIdProject() );
			Platform.runLater( () ->  this.main.updateTasks(taskList));
		}else if(this.data instanceof Person){
			this.throwException();
		}else if(this.data instanceof Team){
			this.throwException();
		}else if(this.data instanceof Project){
			this.throwException();
		}
	}
	
	private void throwException(){
		try{
			throw new Exception("Brak obslugi zadania");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
