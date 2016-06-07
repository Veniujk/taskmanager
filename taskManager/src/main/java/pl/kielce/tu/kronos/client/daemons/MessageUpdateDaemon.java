package pl.kielce.tu.kronos.client.daemons;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.tableClasses.Message;
import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.model.Response;

public class MessageUpdateDaemon implements Runnable{

	private final int REFRESH_SECONDS_INTERVAL;
	private MainController main;
	private int idTask;
	private Date lastRequestTime;
	
	public MessageUpdateDaemon(MainController main) {
		this.main = main;
		this.REFRESH_SECONDS_INTERVAL = 1;
		this.lastRequestTime = new Date();
	}

	@Override
	public void run() {
		Message m = new Message(0, "", 0, this.idTask, this.lastRequestTime, "");
		for(;;){
			try {
				Thread.sleep(REFRESH_SECONDS_INTERVAL * 1000);
				if(this.idTask > 0){
					m.setIdTask(this.idTask);
					m.setCreatedAt(this.lastRequestTime);
					if(this.lastRequestTime != null){
						Response r = ServerConnection.getServer().sendRequest(new Request("Message", "HEAD", m.JSONExport()));
						if(r.getCode() == 302){
							this.main.generateMessages(this.idTask);
							this.lastRequestTime = r.getResponseTime();
						}
					}else{
						this.lastRequestTime = new Date();
						this.main.generateMessages(this.idTask);
					}
				}
			} catch (InterruptedException e) {
//				this.idTask = 0;
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
	
	public synchronized void setIdTask(int idTask){
		this.idTask = idTask;
		
	}
}
