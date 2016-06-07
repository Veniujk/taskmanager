package pl.kielce.tu.kronos.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TableStateWatcher {
	
	private Map<Integer, Date> messageUpdateMap;
	private Map<Integer, Date> taskUpdateMap;
	
	public TableStateWatcher(){
		this.messageUpdateMap = new HashMap<>();
		this.taskUpdateMap = new HashMap<>();
	}
	
	public synchronized void addTaskChanges(int idProject){
		this.taskUpdateMap.put(idProject, new Date());
	}
	
	public synchronized void addMessageChanges(int idTask){
		this.messageUpdateMap.put(idTask, new Date());
	}
	
	public synchronized boolean isTaskChanged(int idProject, Date date){
		if(this.taskUpdateMap.containsKey(idProject)){
			if(this.taskUpdateMap.get(idProject).getTime() > date.getTime()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public synchronized boolean isMessageChanged(int idTask, Date date){
		if(this.messageUpdateMap.containsKey(idTask)){
			if(this.messageUpdateMap.get(idTask).getTime() > date.getTime()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

}
