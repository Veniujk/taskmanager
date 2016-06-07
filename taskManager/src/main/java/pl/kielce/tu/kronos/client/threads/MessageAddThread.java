package pl.kielce.tu.kronos.client.threads;

import java.util.List;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.tableClasses.Message;

public class MessageAddThread implements Runnable{
	
	private String content;
	private int idTask;
	private MainController main;

	public MessageAddThread(MainController main, String content, int idTask) {
		this.content = content;
		this.idTask = idTask;
		this.main = main;
	}

	@Override
	public void run() {
		Message m = new Message(this.idTask, this.content);
		boolean success = m.send();
		if(success){
			List<Message> messageList = Message.getMessages(this.idTask);
			Platform.runLater(() -> this.main.updateMessages(messageList));
		}
	}
}
