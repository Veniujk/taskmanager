package pl.kielce.tu.kronos.client.threads;

import javafx.application.Platform;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Person;

public class PersonEditThread implements Runnable{
	
	SettingsController settings;
	Person user;
	
	public PersonEditThread(SettingsController settings, Person user) {
		this.user = user;
		this.settings = settings;
	}



	@Override
	public void run() {
		boolean isSuccess = this.user.update();
		if(isSuccess){
			Platform.runLater(() -> this.settings.setUserLabeltext("Zaktualizowano"));
		}else{
			Platform.runLater(() -> this.settings.setUserLabeltext("Blad"));
		}
	}
}
