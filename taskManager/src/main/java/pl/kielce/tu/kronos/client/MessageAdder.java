package pl.kielce.tu.kronos.client;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.tableClasses.Message;
import pl.kielce.tu.kronos.model.Response;
import javafx.scene.control.TitledPane;
import javafx.geometry.Pos;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

@SuppressWarnings("restriction")
/**
 * 
 * @author Dawid Kij
 *Class add message to TreeView references to clicked task in MainTable (main window)
 */
public class MessageAdder {
	List<Message> messageList = new ArrayList<>();
/**
 * add massages and check id task in message = id task clicked by user, befor add clear old content in VBox
 * @param messageVBox
 * @param message
 * @return
 */
	@Deprecated
	public void generateMessages(VBox messageVBox,int idTaskClicked)
	{
		this.messageList=Message.getMessages(idTaskClicked);
		messageVBox.getChildren().clear();//jezeli ma cos do dodania to czysci poprzednia zawartosc
		for(Message m:this.messageList)
		{
			String title ="wiadomosc od "+m.getUser() +"\n data: "+m.getCreatedAt().toString();
			Label lbl = new Label(title);
			lbl.setAlignment(Pos.CENTER);
			lbl.setWrapText(false);
			lbl.getStyleClass().add("myClass");
			Label contentLabel = new Label(m.getContent());
			contentLabel.setWrapText(true);
			contentLabel.setPrefWidth(290);
			contentLabel.setPadding(new Insets(5,20,5,20));
			TitledPane titledPane= new TitledPane("wiadomosc",contentLabel);
			titledPane.setText(title);
			messageVBox.getChildren().add(titledPane);
			messageVBox.setAlignment(Pos.TOP_CENTER);
		}
	}
	
	public void generateMessages(VBox messageVBox, List<Message> messageList){
		messageVBox.getChildren().clear();
		for(Message m: messageList){
			String title =m.getUser() +"\t"+m.getCreatedAt().toString();
			Label lbl = new Label(title);
			lbl.setAlignment(Pos.CENTER);
			lbl.setWrapText(false);
			lbl.getStyleClass().add("myClass");
			Label contentLabel = new Label(m.getContent());
			contentLabel.setWrapText(true);
			contentLabel.setPrefWidth(290);
			contentLabel.setPadding(new Insets(5,20,5,20));
			TitledPane titledPane= new TitledPane("wiadomosc",contentLabel);
			titledPane.setText(title);
			messageVBox.getChildren().add(titledPane);
			messageVBox.setAlignment(Pos.TOP_CENTER);
		}
	}

}
