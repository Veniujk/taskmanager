package pl.kielce.tu.kronos.client.threads;

import java.io.IOException;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.LoginController;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.model.User;

public class LoginThread implements Runnable{

	private LoginController loginController;
	private User user;
	private int port;
	private String ip;
	
	public LoginThread(LoginController loginController, User user, String ip, int port) {
		this.loginController = loginController;
		this.user = user;
		this.port = port;
		this.ip = ip;
	}

	@Override
	public void run() {
		try {
			ServerConnection.getServer(this.ip, this.port);
			boolean isLogged = this.user.login();
			if(isLogged){
				Platform.runLater(() ->  this.loginController.loginSuccess()); 
			}else{
				Platform.runLater(() ->  this.loginController.loginFail(true)); 
			}
		} catch (IOException e) {
			Platform.runLater(() ->  this.loginController.loginFail(false)); 
		}
		
	}
}
