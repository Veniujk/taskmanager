package pl.kielce.tu.kronos.client.threads;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Person;

public class PersonGetThread implements Runnable{
	
	private SettingsController controller;
	private int idTeam;
	
	
	public PersonGetThread(SettingsController controller, int idTeam) {
		this.controller = controller;
		this.idTeam = idTeam;
	}


	@Override
	public void run() {
		List<Person> list;
		List<Person> allPeople;
		if(this.idTeam > 0){
			list = Person.getPersonsFromTeam(idTeam);
			allPeople = Person.getAllPersons();
		}else{
			list = null;
			allPeople = Person.getAllPersons();
			ObservableList<Person> teamMemberList = FXCollections.observableArrayList(allPeople);
			Platform.runLater(()->this.controller.teamLeaderChoiceBox.setItems(teamMemberList));
			Platform.runLater(()->this.controller.personListView2.setItems(teamMemberList));
		}
		Platform.runLater( () -> this.controller.setPersonList(allPeople, list,this.idTeam) );
	}
}
