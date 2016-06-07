package pl.kielce.tu.kronos.client.daemons;

import pl.kielce.tu.kronos.client.MainController;

public class UpdateDeamon{

	private Thread treeUpdateThread;
	private Thread taskUpdateThread;
	private Thread messageUpdateThread;
	private TreeViewUpdateDaemon treeDaemon;
	private TaskUpdateDaemon taskDaemon;
	private MessageUpdateDaemon messageDaemon;
	
	public UpdateDeamon(MainController main) {
		this.treeDaemon = new TreeViewUpdateDaemon(main);
		this.taskDaemon = new TaskUpdateDaemon(main);
		this.messageDaemon = new MessageUpdateDaemon(main);
		
		this.treeUpdateThread = new Thread(treeDaemon);
		this.taskUpdateThread = new Thread(taskDaemon);
		this.messageUpdateThread = new Thread(messageDaemon);
		
		this.taskUpdateThread.setDaemon(true);
		this.treeUpdateThread.setDaemon(true);
		this.messageUpdateThread.setDaemon(true);
		
		this.treeUpdateThread.start();
		this.taskUpdateThread.start();
		this.messageUpdateThread.start();
	}
	
	public void resetCounters(){
		this.treeUpdateThread.interrupt();
		this.taskUpdateThread.interrupt();
		this.messageUpdateThread.interrupt();
	}
	
	public void setIdTask(int idTask){
		this.messageUpdateThread.interrupt();
		this.messageDaemon.setIdTask(idTask);
	}
	
	public void setIdProject(int idProject){
		this.taskUpdateThread.interrupt();
		this.taskDaemon.setIdProject(idProject);
	}
	
}
