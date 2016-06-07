package pl.kielce.tu.kronos.client;

import java.io.IOException;

import javax.naming.Context;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


/**
 * 
 * @author Dawid Kij
 * @author Bartosz Piskiewicz
 *Class used to manipulation of stages
 */
@SuppressWarnings("restriction")

public class StageLoader {
/**
 * Open new stage
 * @param stageLoading it contains the name of FXML file for example "main.fxml"
 * @return 
 */
	public Stage openStage(String stageLoading)
	{
		Parent root;
		Stage mainStage = new Stage();
		try {
			root=FXMLLoader.load(getClass().getResource(stageLoading));
			mainStage.setScene(new Scene(root));
			mainStage.initModality(Modality.APPLICATION_MODAL);
			mainStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return mainStage;
	}
	
	/**
	 * Opens stage with given reference to MainController object
	 * @param stageLoading fxml file name
	 * @param main regerence to MainController object
	 * @return true on success, false when IOException
	 */
	public boolean openStage(String stageLoading, Initializable controller){
		try{
			Stage mainStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(stageLoading));
			loader.setController(controller);
			Pane layout = loader.load();
			mainStage.setScene(new Scene(layout));
			mainStage.initModality(Modality.APPLICATION_MODAL);
			mainStage.show();
			return true;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean openConfirmStage(String stageLoading, Initializable controller){
		try{
			Stage mainStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(stageLoading));
			loader.setController(controller);
			Pane layout = loader.load();
			mainStage.setScene(new Scene(layout));
			mainStage.initModality(Modality.APPLICATION_MODAL);
			mainStage.showAndWait();
			return true;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Switch the stages after logged in
	 * @param loginButton 
	 * @return
	 */
	public boolean changeStage(Button loginButton)
	{
		Stage stage=(Stage)loginButton.getScene().getWindow();
		stage.hide();
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
			root = loader.load();
			MainController controller = (MainController)loader.getController();
			stage.setScene(new Scene(root,1200,700));
			stage.setOnCloseRequest( (e) -> {
				boolean close = ConfirmBox.confirm("Alert", "Close application?");
				if(!close){
					e.consume();
				}else{
					controller.threadPool.shutdown();
					try {
						ServerConnection.getServer().close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} );
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
