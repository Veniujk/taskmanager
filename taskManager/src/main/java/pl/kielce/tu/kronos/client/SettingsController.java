package pl.kielce.tu.kronos.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import pl.kielce.tu.kronos.client.model.User;
import pl.kielce.tu.kronos.client.tableClasses.Person;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import pl.kielce.tu.kronos.client.threads.EditTeamMembersThread;
import pl.kielce.tu.kronos.client.threads.PersonEditThread;
import pl.kielce.tu.kronos.client.threads.PersonGetThread;
import pl.kielce.tu.kronos.client.threads.ProjectGetThread;
import pl.kielce.tu.kronos.client.threads.AddProjectThread;
import pl.kielce.tu.kronos.client.threads.EditProjectThread;
import pl.kielce.tu.kronos.client.threads.DeleteProjectThread;
import pl.kielce.tu.kronos.client.threads.PersonAddThread;
import pl.kielce.tu.kronos.client.threads.PersonDeleteThread;
import pl.kielce.tu.kronos.client.threads.AddTeamThread;
import pl.kielce.tu.kronos.client.threads.TeamEditThread;
import pl.kielce.tu.kronos.client.threads.TeamRemoveThread;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.CheckBox;

@SuppressWarnings("restriction")

public class SettingsController implements Initializable {
	@FXML
	TextField userNameTextField;
	@FXML
	TextField userPasswordTextField;
	@FXML
	TextField userPasswordConfirmTextField;
	@FXML
	Button buttonUserSettingsConfirm;
    @FXML
    private Label userLabel;
	@FXML
	TabPane tabPane;
	@FXML
	ChoiceBox<Team> teamChoiceBox;
	@FXML
	ChoiceBox<Team> teamChoiceBox2;
	@FXML
	ChoiceBox<Team> teamChoiceBox3;
	@FXML
	ChoiceBox<Team> teamChoiceBox4;
	@FXML
	public ChoiceBox<Project> projectChoiceBox;
	@FXML
	ListView<Person> personListView;
	@FXML
	ChoiceBox<Person> personChoiceBox;
	@FXML
	Button confirmTeam;
	@FXML
	Button addPerson;
	@FXML
	Button deletePerson;
	@FXML
	Button loadProjects;
	@FXML
	Button addProject;
	@FXML
	TextField projectNameTextField;
	@FXML
	TextField projectNameTextField2;
	@FXML
	Button updateProject;
	@FXML 
	Button deleteProject;
	//dodaj uzytkownika admin
	@FXML
	TextField userLoginTextField2;
	@FXML
	TextField userPasswordTextField2;
	@FXML
	TextField userNameTextField2;
	@FXML
	CheckBox isAdminCheckBox;
	@FXML
	Button addUserButton;
	
	@FXML
	Button deleteUserButton;
	@FXML
	public ListView<Person> personListView2;
	
	@FXML
	TextField teamNameTextField;
	@FXML
	public ChoiceBox<Person> teamLeaderChoiceBox;
	@FXML
	Button addTeamButton;
	
	@FXML
	ChoiceBox<Team> teamChoiceBox5;
	@FXML
	TextField teamNameTextField2;
	@FXML
	ChoiceBox<Person> teamLeaderChoiceBox2;
	@FXML
	Button selectionTeamButton;
	@FXML
	Button updateTeamButton;
	@FXML
	Button deleteTeamButton;
	
	
	boolean isAdmin = User.isAdmin;
	boolean isTeamLeader = true;
	public static boolean clickFromContext;
	private MainController main;
	public ObservableList<Person> allPersonList;
	private Person editedPerson;
	
	public SettingsController(MainController mainController) {
		this.main = mainController;
	}

	@FXML
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		ObservableList<Tab> oList = tabPane.getTabs();
		for (Tab x : oList) {
			x.setDisable(true);
		}
		oList.get(0).setDisable(false);
		if (isTeamLeader) {
			oList.get(1).setDisable(false);
		}
		if (isAdmin) {
			oList.get(2).setDisable(false);
		}
		if (clickFromContext) {
			tabPane.getSelectionModel().select(1);
		}
		teamChoiceBox.setItems(getTeamsChoiceBox());
		teamChoiceBox2.setItems(getTeamsChoiceBox());
		teamChoiceBox3.setItems(getTeamsChoiceBox());
		teamChoiceBox4.setItems(getTeamsChoiceBox());
		this.userLabel.setVisible(false);
		this.deletePerson.setDisable(true);
		this.addPerson.setDisable(true);
		
		personChoiceBox.setDisable(true);
		projectChoiceBox.setDisable(true);
		
		
		
		
		
		teamChoiceBox5.setItems(getTeamsChoiceBox());
		
		teamNameTextField2.setDisable(true);
		teamLeaderChoiceBox2.setDisable(true);
	}

	@FXML
	public void adminTabselected()
	{
		this.main.threadPool.submit(new PersonGetThread(this,0));
		
//		personListView2.setItems(this.allPersonList);

//		teamLeaderChoiceBox.setItems(this.allPersonList);
	}
	public void buttonDeleteUserClicked()
	{
		deleteUserButton.setDisable(true);
		int idPerson = personListView2.getSelectionModel().getSelectedItem().getId();
		this.main.threadPool.submit(new PersonDeleteThread(this,idPerson));
	}
	@FXML
	public void loadProjectsClicked() {
		int idTeam = teamChoiceBox3.getValue().getId();
//		projectChoiceBox.setItems(getProjectsChoiceBox(idTeam));
		this.main.threadPool.submit(new ProjectGetThread(this,idTeam));
	}
	

	public void buttonConfirmTeamClicked() {
		int idTeam = teamChoiceBox.getValue().getId();
		this.personChoiceBox.getItems().clear();
		this.main.threadPool.submit(new PersonGetThread(this, idTeam));
		personChoiceBox.setDisable(false);
	}
	/*	@FXML
	public ObservableList<Person> getAllPersons()
	{
		List<Person> list = new ArrayList<>();
		list = Person.getAllPersons();
		ObservableList<Person> oList = FXCollections.observableArrayList();
		for (Person x : list) {
			oList.add(x);
		}
		return oList;
	}*/
	@FXML
	@Deprecated
	private ObservableList<Person> getPersonsChoiceBox() {
		List<Person> list = new ArrayList<>();
		list = Person.getAllPersons();
		ObservableList<Person> oList = FXCollections.observableArrayList();
		for (Person x : list) {
			oList.add(x);
		}
		return oList;
	}

	private ObservableList<Team> getTeamsChoiceBox() {
		List<Team> teamList = this.main.getTeams();
		ObservableList<Team> oList = FXCollections.observableArrayList();
		oList.addAll(teamList);
		return oList;
	}

	@FXML
	private void requestPersonsListView(int idTeam) {
		this.main.threadPool.submit(new PersonGetThread(this, idTeam));
	}
	
	public void setPersonList(List<Person> allList, List<Person> teamPersonList, int idTeam){
		
		
		if(idTeam>0){
		ObservableList<Person> notTeamPeople = FXCollections.observableArrayList(allList);
		ObservableList<Person> teamMemberList = FXCollections.observableArrayList(teamPersonList);
		notTeamPeople.removeAll(teamMemberList);
		this.personChoiceBox.setItems(notTeamPeople);
		this.personListView.setItems(teamMemberList);
		this.addPerson.setDisable(false);
		this.deletePerson.setDisable(false);
		};
	}

	
	
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isTeamLeader() {
		return isTeamLeader;
	}
	public void setTeamLeader(boolean isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}

	@FXML
	public void buttonAddPersonClicked() {
		this.addPerson.setDisable(true);
		this.deletePerson.setDisable(true);
		this.editedPerson = this.personChoiceBox.getValue();
		int idPerson = this.editedPerson.getId();
		int idTeam = teamChoiceBox.getValue().getId();
		this.main.threadPool.submit(new EditTeamMembersThread(this, "Add", idTeam, idPerson));
	}
	@FXML
	public void buttonAddTeamClicked(){
		this.addTeamButton.setDisable(true);
		int idTeamLeader = this.teamLeaderChoiceBox.getValue().getId();
		String name = this.teamNameTextField.getText();
		this.main.threadPool.submit(new AddTeamThread(this,main, name, idTeamLeader));
		
	}

	@FXML
	public void buttonDeletePersonClicked(){
		this.addPerson.setDisable(true);
		this.deletePerson.setDisable(true);
		this.editedPerson = this.personListView.getSelectionModel().getSelectedItem();
		int idPerson = this.editedPerson.getId();
		int idTeam = teamChoiceBox.getValue().getId();
		this.main.threadPool.submit(new EditTeamMembersThread(this, "Remove", idTeam, idPerson));
	}
	
	@FXML
	public void buttonUpdateProjectClicked()
	{
		updateProject.setDisable(true);
		String name;
		if(projectNameTextField2.getText().trim().equals(""))
		{
			name = projectChoiceBox.getValue().getName();
		}
		else {
			name = projectNameTextField2.getText();
		}
		
		int idTeam = teamChoiceBox4.getValue().getId();
		int idProject = projectChoiceBox.getValue().getId();
		this.main.threadPool.submit(new EditProjectThread(this, main, name, idTeam, idProject));
	}
	@FXML
	public void buttonDeleteProjectClicked()
	{
		deleteProject.setDisable(true);
		int idProject = projectChoiceBox.getValue().getId();
		this.main.threadPool.submit(new DeleteProjectThread(this, main, idProject));
		
	}
	@FXML
	public void buttonAddProjectClicked()
	{
		addProject.setDisable(true);
		String name=projectNameTextField.getText();
		int idTeam = teamChoiceBox2.getValue().getId();
		this.main.threadPool.submit(new AddProjectThread(this,this.main, name, idTeam));
		
	}
	public synchronized void updatePersonList(boolean isAdded){
		if(isAdded){
			this.personListView.getItems().add(this.editedPerson);
			this.personChoiceBox.getItems().remove(this.editedPerson);
		}else{
			this.personChoiceBox.getItems().add(this.editedPerson);
			this.personListView.getItems().remove(this.editedPerson);
		}
		this.editedPerson = null;
		this.addPerson.setDisable(false);
		this.deletePerson.setDisable(false);
	}

	@FXML
	public void buttonUserSettingsClicked() {
		this.userLabel.setVisible(false);
		if (userPasswordTextField.getText().equals(userPasswordConfirmTextField.getText())) {
			this.buttonUserSettingsConfirm.setDisable(true);
			String password = this.userPasswordTextField.getText().trim();
			String name = this.userNameTextField.getText().trim();
			this.main.threadPool.submit(new PersonEditThread(this, new Person(0, null, password, name, false)));
		} else {
			this.userLabel.setText("Haslo sie rozni");
			this.userLabel.setVisible(true);
		}
	}
	@FXML
	public void buttonAddUserClicked()
	{
		addUserButton.setDisable(true);
		String username = userLoginTextField2.getText();
		String password = userPasswordTextField2.getText();
		String name = userNameTextField2.getText();
		boolean isAdmin = isAdminCheckBox.isSelected();
		this.main.threadPool.submit(new PersonAddThread(this, username, password, name, isAdmin));
		
	
	}
	@FXML
	public void buttonSelectionTeamClicked()
	{
		
		String name = teamChoiceBox5.getValue().getName();
		int idTeamLeader = teamChoiceBox5.getValue().getIdTeamLeader();
		teamNameTextField2.setText(name);
		ObservableList<Person> oList = FXCollections.observableArrayList();
		oList = this.teamLeaderChoiceBox.getItems();
		Person person =null;
		for(Person x:oList)
		{
			if(x.getId() == idTeamLeader)
			{
				 person = x;
			}
		}
		teamLeaderChoiceBox2.setItems(oList);
		teamLeaderChoiceBox2.getSelectionModel().select(person);
		teamNameTextField2.setDisable(false);
		teamLeaderChoiceBox2.setDisable(false);
		
	}
	@FXML
	public void buttonUpdateTeamClicked()
	{
		updateTeamButton.setDisable(true);
		int idTeam = teamChoiceBox5.getValue().getId();
		String name = teamNameTextField2.getText();
		int idTeamLeader = this.teamLeaderChoiceBox2.getValue().getId();
		this.main.threadPool.submit(new TeamEditThread(this, this.main, idTeam, name, idTeamLeader));
	}
	@FXML
	public void buttonDeleteTeamClicked()
	{
		deleteTeamButton.setDisable(true);
		int idTeam = teamChoiceBox5.getValue().getId();
		this.main.threadPool.submit(new TeamRemoveThread(this, this.main, idTeam));
	}
	public synchronized void setUserLabeltext(String text){
		this.userLabel.setText(text);
		this.userLabel.setVisible(true);
		this.buttonUserSettingsConfirm.setDisable(false);
	}
	public void enableButtonAddProject()
	{
		addProject.setDisable(false);
	}
	public void enableButtonUpdateProject()
	{
		updateProject.setDisable(false);
	}
	public void enableButtonDeleteProject()
	{
		deleteProject.setDisable(false);
	}
	public void enableButtonAddUser()
	{
		addUserButton.setDisable(false);
		this.main.threadPool.submit(new PersonGetThread(this,0));
	}
	public void enableButtonDeleteUser()
	{
		deleteUserButton.setDisable(false);
		this.main.threadPool.submit(new PersonGetThread(this,0));
	}
	public void enableButtonAddTeam()
	{
		addTeamButton.setDisable(false);
	}

	public void enableButtonUpdateTeam()
	{
		updateTeamButton.setDisable(false);
	}
	public void enableButtonDeleteTeam()
	{
		deleteTeamButton.setDisable(false);
	}
	public void teamChoiceBoxesRefresh()
	{
		List<Team> list = Team.getTeams();
		
		ObservableList<Team> oList = FXCollections.observableArrayList();
		for(Team x:list)
		{
			oList.add(x);
		}
	

		teamChoiceBox.setItems(oList);
		teamChoiceBox2.setItems(oList);
		teamChoiceBox3.setItems(oList);
		teamChoiceBox4.setItems(oList);
		teamChoiceBox5.setItems(oList);
	}
	public void enableProjectChoiceBox()
	{
		this.projectChoiceBox.setDisable(false);
	}
}
