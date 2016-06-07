package pl.kielce.tu.kronos.client.threads;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Person;

public class PersonDeleteThread implements Runnable{
SettingsController controller;
int idPerson;
public PersonDeleteThread(SettingsController controller, int idPerson) {
	super();
	this.controller = controller;
	this.idPerson = idPerson;
}
@Override
public void run() {
	Person.deletePerson(this.idPerson);
	 Platform.runLater(() ->this.controller.enableButtonDeleteUser());
	
	
}
	
}
