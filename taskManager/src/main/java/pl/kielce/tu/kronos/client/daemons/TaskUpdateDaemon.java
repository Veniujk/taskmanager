package pl.kielce.tu.kronos.client.daemons;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.tableClasses.Task;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;

public class TaskUpdateDaemon implements Runnable{

	private final int REFRESH_SECONDS_INTERVAL;
	private MainController main;
	private int idProject;
	private Date lastRequestTime;
	
	public TaskUpdateDaemon(MainController main) {
		this.main = main;
		this.REFRESH_SECONDS_INTERVAL = 2;
		this.lastRequestTime = new Date();
	}

	@Override
	public void run() {
		Task task = new Task(0, "", "", null, this.lastRequestTime, null, 0, this.idProject, "");
		for(;;){
			try {
				Thread.sleep(REFRESH_SECONDS_INTERVAL * 1000);
				if(this.idProject > 0){
					task.setIdProject(this.idProject);
					task.setUpdatedAt(this.lastRequestTime);
					if(this.lastRequestTime != null){
						Response r = ServerConnection.getServer().sendRequest(new Request("Task", "HEAD", task.JSONExport()));
						if(r.getCode() == 302){
							this.main.generateTasks(this.idProject);
						}
						this.lastRequestTime = r.getResponseTime();
					}else{
						this.lastRequestTime = new Date();
						this.main.generateTasks(this.idProject);
					}
				}
			} catch (InterruptedException e) {
				this.lastRequestTime = new Date();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void setIdProject(int idProject){
		this.idProject = idProject;
	}
}
