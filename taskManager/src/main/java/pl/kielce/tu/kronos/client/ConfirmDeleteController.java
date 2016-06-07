package pl.kielce.tu.kronos.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
@SuppressWarnings("restriction")
public class ConfirmDeleteController {
	@FXML Button yesButton;
	@FXML Button noButton;
	public static boolean agree = false;
	public void yesButtonClicked()
	{
		agree=true;
		Stage window = (Stage)noButton.getScene().getWindow();
		agree=false;
		window.close();
	}
	public void noButtonClicked()
	{
		Stage window = (Stage)noButton.getScene().getWindow();
		agree=false;
		window.close();
	}

	
	
}
