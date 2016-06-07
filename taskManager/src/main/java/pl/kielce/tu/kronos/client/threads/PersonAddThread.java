package pl.kielce.tu.kronos.client.threads;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Person;

public class PersonAddThread implements Runnable{
SettingsController controller;
String username;
String password;
String name;
boolean isAdmin;

public PersonAddThread(SettingsController controller, String username, String password, String name, boolean isAdmin) {
	super();
	this.controller = controller;
	this.username = username;
	this.password = password;
	this.name = name;
	this.isAdmin = isAdmin;
}

@Override
public void run() {
	Person.addPerson(username, password, name, isAdmin);
	Platform.runLater(() ->controller.enableButtonAddUser());
}
	

}
