package pl.kielce.tu.kronos.client.threads;

import java.util.List;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.tableClasses.Message;

public class MessageGetThread implements Runnable{
	
	private MainController main;
	private int idTask;
	
	public MessageGetThread(MainController main, int idTask) {
		this.main = main;
		this.idTask = idTask;
	}

	@Override
	public void run() {
		List<Message> messageList = Message.getMessages(this.idTask);
		Platform.runLater(() -> this.main.updateMessages(messageList));
	}
}
