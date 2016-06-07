package pl.kielce.tu.kronos.client;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class ConfirmBox {
	private static boolean answer;
	
	public static boolean confirm(String title, String message){
		Stage confirmStage = new Stage();
		confirmStage.initModality(Modality.APPLICATION_MODAL);
		confirmStage.setTitle(title);
		confirmStage.setMinHeight(150);
		confirmStage.setMinWidth(300);
		//boolean answer = false;
		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");
		
		noButton.setOnAction(e -> {
			answer = false;
			confirmStage.close();
			
		});
		yesButton.setOnAction(e -> {
			answer = true;
			confirmStage.close();
		});
		Label label = new Label(message);
		VBox layout = new VBox(10);
		HBox confirmBox = new HBox(10);
		confirmBox.getChildren().add(yesButton);
		confirmBox.getChildren().add(noButton);
		confirmBox.setAlignment(Pos.CENTER);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, confirmBox);
		Scene scene = new Scene(layout);
		confirmStage.setScene(scene);
		confirmStage.showAndWait();
		return answer;
	}
}
