package pl.kielce.tu.kronos.client;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import pl.kielce.tu.kronos.client.tableClasses.Person;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import pl.kielce.tu.kronos.client.tableClasses.Task;
import pl.kielce.tu.kronos.client.threads.TaskEditThread;
import pl.kielce.tu.kronos.client.threads.TaskGetThread;
import pl.kielce.tu.kronos.client.threads.TaskGetThread2;
import pl.kielce.tu.kronos.model.TreeViewAccess;
import javafx.scene.control.ListView;
@SuppressWarnings("restriction")
public class AddTaskController implements Initializable {
	
	private MainController main;
	
	@FXML ChoiceBox<Person> choiceBox;
	@FXML Button addTaskButtonConfirm;
	@FXML TextField textField;
	@FXML DatePicker datePicker;
	
	@FXML ListView<Task> allTask;
	@FXML ListView <Task>parentSelection;
	@FXML Button selectionButton;
	
	public void initialize(URL location, ResourceBundle resources) {
		choiceBox.setItems(this.getPersonList());
		int idProject = this.main.getSelectedTreeItem().getId();
		this.main.threadPool.submit(new TaskGetThread2(this,idProject));
	}

	public AddTaskController(MainController main) {
		this.main = main;
	}

	/**
	 * method resposible for addTask
	 * @throws ParseException 
	 */
	@FXML
	public void addTaskActionConfirm() throws ParseException {
		if(this.datePicker.getValue().toString().trim().equals("") || this.choiceBox.getSelectionModel().getSelectedItem() == null){
			return;
		}
		Stage addTaskStage = (Stage) addTaskButtonConfirm.getScene().getWindow();
		String taskName = textField.getText();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date deadline = sdf.parse(datePicker.getValue().toString());
		Person person = choiceBox.getValue();
		int idProject = this.main.getSelectedTreeItem().getId();
		Task task = new Task(0, taskName, "", null, null, deadline, person.getId(), idProject, "");
		List<Integer> parents = new ArrayList<>();
		for(Task t: this.parentSelection.getItems()){
			parents.add(t.getId());
		}
		this.main.threadPool.submit(new TaskEditThread(this.main, task, parents));
		addTaskStage.close();
	}
	@FXML
	public ObservableList<Person> getPersonList(){
		TreeViewAccess treeItem = this.main.getSelectedTreeItem();
		List<Person> list = new ArrayList<>();
		if(treeItem instanceof Project){
			list = Person.getPersons(treeItem.getId());
		}else{
			list = Person.getPersonsFromTeam(treeItem.getId());
		}
		ObservableList<Person> observableList = FXCollections.observableArrayList();
		observableList.addAll(list);
		return observableList;
	}
	@FXML
	public void getAllTeamTask(ObservableList<Task> oList)
	{
		this.allTask.setItems(oList);
	}

	public void buttonSelectionClicked() {
		Task selectedTask = this.allTask.getSelectionModel().getSelectedItem();
		
		this.allTask.getItems().remove(selectedTask);
		this.parentSelection.getItems().add(selectedTask);
	}
	public void resetTasks()
	{
		this.parentSelection.getItems().clear();
		int idProject = this.main.getSelectedTreeItem().getId();
		this.main.threadPool.submit(new TaskGetThread2(this,idProject));
	}
}
