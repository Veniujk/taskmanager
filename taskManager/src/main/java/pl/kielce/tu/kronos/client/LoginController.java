package pl.kielce.tu.kronos.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.kielce.tu.kronos.client.model.ConnectionProperties;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.model.User;
import pl.kielce.tu.kronos.client.threads.LoginThread;
@SuppressWarnings("restriction")
/**
 * 
 * @author Dawid Kij
 * @author Bartosz Piskiewicz
 *Class used to manipulate the Login window
 */
public class LoginController  implements Initializable 
{
	
	@FXML TextField ipField;
	@FXML TextField portField;
	@FXML TextField usernameField;
	@FXML Button loginButton;
	@FXML PasswordField passwordField;
	@FXML private Label invalidDataLabel;
	
	private String username;
	private String ip;
	private int port;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConnectionProperties properties = new ConnectionProperties();
		invalidDataLabel.setVisible(false);
		ipField.setText(properties.getIp());
		portField.setText(Integer.toString(properties.getPort()));
		usernameField.setText(properties.getUsername());
	}
	/**
	 * method used to login in | first read connectionConfig to help user with log in, connect and log in after that loged in save the configuration
	 * @throws IOException
	 */
	 public void loginButtonAction(){
		this.port = Integer.parseInt(this.portField.getText());
		this.ip = this.ipField.getText();
		this.username = this.usernameField.getText();
		String password = this.passwordField.getText();
		invalidDataLabel.setVisible(false);
		User user = new User(username, password);
		new Thread(new LoginThread(this, user, this.ip, this.port)).start();
	}
	 
	 public synchronized void loginSuccess(){
		new StageLoader().changeStage(this.loginButton);
		try{
			new ConnectionProperties(this.username, this.ip, this.port);
		}catch(IOException e){
			e.printStackTrace();
		}
	 }
	 
	public synchronized void loginFail(boolean passwordIncorrect){
		invalidDataLabel.setVisible(true);
		if(passwordIncorrect){
			invalidDataLabel.setText("Niepoprawne dane");
		}else{
			invalidDataLabel.setText("B³¹d po³¹czenia");
		}
		try {
			ServerConnection.getServer().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
}
