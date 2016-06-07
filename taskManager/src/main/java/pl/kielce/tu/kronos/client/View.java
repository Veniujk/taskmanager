package pl.kielce.tu.kronos.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * 
 * @author Dawid Kij
 *Class used to start the application
 */
public class View extends Application 
{
	Stage window;
	Scene sceneLogin,sceneMain;
	Parent root;
	final private String LOGIN_FXML= "login.fxml";

	public void initialize(URL location, ResourceBundle resources)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		root=FXMLLoader.load(getClass().getResource("login.fxml"));
		window=primaryStage;
		window.setTitle("Task Manager - Organizer zadan");
		sceneLogin=new Scene(root,243,150);
		window.setScene(sceneLogin);
		window.show();
	}

	public static void main(String[] args) 
	{
		launch(args);
	}	
}
