package pl.kielce.tu.kronos.client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.kielce.tu.kronos.client.model.ServerConnection;
@SuppressWarnings("restriction")

/**
 * 
 * @author Dawid Kij
 *Class used as controler to manipulate the confirmexit window
 */
public class ConfirmExitController  implements Initializable {
	@FXML Button yesButton;
	@FXML Button noButton;
	private MainController main;
	private WindowEvent closeEvent;
	
	public ConfirmExitController(WindowEvent closeEvent, MainController main) {
		this.closeEvent = closeEvent;
		this.main = main;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.yesButton.setOnAction( (e) -> {
			this.main.threadPool.shutdown();
			try {
				ServerConnection.getServer().close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.noButton.setOnAction( (e) -> {
			this.closeEvent.consume();
		} );
		
	}
	/**
	 * method exit from Application
	 */
	public void yesButtonClicked()
	{
		this.main.threadPool.shutdown();
//		System.exit(1);
	}
	/**
	 * method close the popup window
	 */
	public void noButtonClicked()
	{
		Stage stageConfirm=(Stage)yesButton.getScene().getWindow();
		stageConfirm.close();
	}

	public void confirm(){
		
	}
}
