package pl.kielce.tu.kronos.client;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.kielce.tu.kronos.client.tableClasses.Person;
import pl.kielce.tu.kronos.client.tableClasses.Task;
import pl.kielce.tu.kronos.client.threads.ParentGetThread;
import pl.kielce.tu.kronos.client.threads.TaskEditThread;
import pl.kielce.tu.kronos.client.threads.TaskGetThread;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
@SuppressWarnings("restriction")
/**
 * 
 * @author Dawid Kij
 *Class used to control the edittask.fxml window
 */
public class EditTaskController implements Initializable{

    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<Person> choiceBox;
    @FXML
    private Label taskName;
    @FXML
    private Button editTaskButtonConfirm;
    @FXML private ListView<Task> parentSelection;
    @FXML private ListView<Task> allTasks;
    
    @FXML Button addParentButton;
    @FXML Button removeParentButton;

	private Task task;
	private MainController main;
	
	public EditTaskController(MainController main) {
		this.main = main;
		this.task = this.main.getSelectedTask();
	}
	
	@Override
	@FXML
	public void initialize(URL arg0, ResourceBundle arg1) {
		taskName.setText(this.task.getName());
		choiceBox.setItems(this.getPersonList());
		this.selectProperPerson();
		this.datePicker.setValue(LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(this.task.getDeadline() ) ));
		this.getParentList();
	}
	@FXML
	public void editTaskActionConfirm(){
		if(this.datePicker.getValue().toString().trim().equals("") || this.choiceBox.getSelectionModel().getSelectedItem() == null){
			return;
		}
		Date deadline = Date.from(this.datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		this.task.setDeadline(deadline);
		this.task.setIdPerson( this.choiceBox.getValue().getId());
		List<Integer> parents = new ArrayList<>();
		for(Task t: this.parentSelection.getItems()){
			parents.add(t.getId());
		}
		this.main.threadPool.submit(new TaskEditThread(this.main, this.task, parents));	
		((Stage)this.choiceBox.getScene().getWindow()).close();
	}
	@FXML
	public ObservableList<Person> getPersonList(){
		List<Person> list = new ArrayList<>();
		list = Person.getPersons(this.task.getIdProject());
		ObservableList<Person> observableList = FXCollections.observableArrayList();
		observableList.addAll(list);
		return observableList;
	}
	
	private void selectProperPerson(){
		this.choiceBox.getSelectionModel().selectFirst();
		int i = 0;
		while(this.choiceBox.getItems().size() < i){
			if(this.choiceBox.getSelectionModel().getSelectedItem().getId() == this.task.getIdPerson()){
				break;
			}
			i++;
			this.choiceBox.getSelectionModel().selectNext();
		}
	}
	
	public void addParentButtonClicked(){
		Task t = this.allTasks.getSelectionModel().getSelectedItem();
		this.allTasks.getItems().remove(t);
		this.parentSelection.getItems().add(t);
	}
	
	public void removeParentButtonClicked(){
		Task t = this.parentSelection.getSelectionModel().getSelectedItem();
		this.parentSelection.getItems().remove(t);
		this.allTasks.getItems().add(t);
	}
	
	private void getParentList(){
		this.main.threadPool.submit(new ParentGetThread(this.parentSelection, this.allTasks, this.task));
	}
}
