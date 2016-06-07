package pl.kielce.tu.kronos.client;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;

import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextArea;
import pl.kielce.tu.kronos.model.*;
import pl.kielce.tu.kronos.client.daemons.UpdateDeamon;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.tableClasses.Message;
import pl.kielce.tu.kronos.client.tableClasses.Task;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import pl.kielce.tu.kronos.client.threads.DataDeleteThread;
import pl.kielce.tu.kronos.client.threads.MessageAddThread;
import pl.kielce.tu.kronos.client.threads.MessageGetThread;
import pl.kielce.tu.kronos.client.threads.TaskEditThread;
import pl.kielce.tu.kronos.client.threads.TaskGetThread;
import javafx.scene.control.SplitPane;

@SuppressWarnings("restriction")
public class MainController implements Initializable 
{	
	/**
	 * @author Dawid Kij
	 * Class used as MainController to control main window
	 */
	//@FXML Button addTaskButton;
	@FXML TableView<Task> mainTaskTableView;
	@FXML TreeView<TreeViewAccess> treeView;
	@FXML MenuItem addTaskContextMenu;
	@FXML MenuItem editTaskContextMenu;
	@FXML MenuItem editPersonContextMenu;
	@FXML MenuItem contextMenuStatusActive;
	@FXML MenuItem contextMenuStatusDone;
	@FXML MenuItem contextMenuStatusWaiting;
	@FXML MenuItem contextMenuStatusSuspended;
	@FXML VBox messageVBox;
	@FXML Button exitButton;
	@FXML Button sendButton;
	@FXML TextArea textArea;
	@FXML SplitPane splitPane;
	@FXML Button settingsButton;
	
	final private String SETTINGS_FXML= "settings.fxml";
	final private String ADDTASK_FXML= "addtask.fxml";
	public ExecutorService threadPool = Executors.newSingleThreadExecutor();
	private UpdateDeamon updatingThreads;

	@FXML
	public void initialize(URL location, ResourceBundle resources){
		this.settingsButton.setDisable(true);
		this.generateTree();
		new TableViewCreator().generateTaskTable(this.mainTaskTableView);
		addTaskContextMenu.setOnAction(e->addTaskContextMenu());
		treeView.setOnMouseClicked(e->treeViewClicked());
		this.mainTaskTableView.getSelectionModel().selectedItemProperty().addListener((event, oldVal, newVal) -> this.tableClickedAction(oldVal, newVal));
		this.updatingThreads = new UpdateDeamon(this);
	}
	
	protected void treeViewClicked() {
		if(treeView.getSelectionModel().getSelectedItem() != null && treeView.getSelectionModel().getSelectedItem().getValue() instanceof pl.kielce.tu.kronos.client.tableClasses.Project){
			this.sendButton.setDisable(true);
			int idProject = treeView.getSelectionModel().getSelectedItem().getValue().getId();
			if(idProject != 0){
				this.updatingThreads.resetCounters();
				this.updatingThreads.setIdTask(0);
				this.updatingThreads.setIdProject(idProject);
				this.generateTasks(idProject);
			}
		}
	}
	
	public void addTaskContextMenu() {
		new StageLoader().openStage(ADDTASK_FXML, new AddTaskController(this));
	}
	
	 public void editTaskContextMenu(){
		 new StageLoader().openStage("edittask.fxml", new EditTaskController(this));
	 }
	 
	 public void editPersonContextMenu(){
		 StageLoader editTaskWindow = new StageLoader();
		 editTaskWindow.openStage("edittask.fxml");
	 }
	
	 public void tableClickedAction(Task oldVal, Task newVal){
		if(newVal == null || (oldVal != null && oldVal.equals(newVal))){
			return;
		}
		Task marked = mainTaskTableView.getSelectionModel().getSelectedItem();
		this.sendButton.setDisable(false);
		int idTask = marked.getId();
		if(idTask != 0){
			this.updatingThreads.resetCounters();
			this.updatingThreads.setIdTask(idTask);
			this.generateMessages(idTask);
		}
	 }
	 
	 public void settingsButtonClicked(){
		 SettingsController.clickFromContext=false;
		 StageLoader settingsWindow = new StageLoader();
		 settingsWindow.openStage("settings.fxml", new SettingsController(this));
	 }
	 public void exitButtonClicked(){ 
		 Stage stage = ((Stage)this.exitButton.getScene().getWindow());
		 stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	 }
	 public void sendButtonClicked(){
		 if(!this.textArea.getText().trim().equals("") && this.mainTaskTableView.getSelectionModel().getSelectedItem() != null){
			 this.sendButton.setDisable(true);
			 String content = this.textArea.getText();
			 int idTask = this.mainTaskTableView.getSelectionModel().getSelectedItem().getId();
			 if(idTask != 0){
				 this.threadPool.submit(new MessageAddThread(this, content, idTask));
			 }
			 this.textArea.clear();
		 }
	 }
	 public void deleteTaskContextMenu()
	 {
		 Task t = this.getSelectedTask();
		 boolean delete = ConfirmBox.confirm("Uwaga", "Usunac zaznaczone zadanie?");
			if(delete){
				this.threadPool.submit(new DataDeleteThread(this, t));
			}
	 }
	 
	 public void contextMenuActiveClicked(){
		 Task t = this.getSelectedTask();
		 t.setStatus("active");
		 this.threadPool.submit(new TaskEditThread(this, t));
	 }
	 
	 public void contextMenuDoneClicked(){
		 Task t = this.getSelectedTask();
		 t.setStatus("done");
		 this.threadPool.submit(new TaskEditThread(this, t));
	 }
	 
	 public void contextMenuWaitingClicked(){
		 Task t = this.getSelectedTask();
		 t.setStatus("waiting");
		 this.threadPool.submit(new TaskEditThread(this, t));
	 }
	 
	 public void contextMenuSuspendedClicked(){
		 Task t = this.getSelectedTask();
		 t.setStatus("suspended");
		 this.threadPool.submit(new TaskEditThread(this, t));
	 }
	 
	 public synchronized void updateTasks(List<Task> taskList){
		 Task t = this.getSelectedTask();
		 new TableViewCreator().generateTaskTable(this.mainTaskTableView, taskList);
		 this.messageVBox.getChildren().clear();
		 this.mainTaskTableView.getSelectionModel().select(t);
	 }
	 
	 public synchronized void updateMessages(List<Message> messageList){
		 new MessageAdder().generateMessages(this.messageVBox, messageList);
		 this.sendButton.setDisable(false);
	 }
	 
	 public Task getSelectedTask(){
		 try {
			 if(this.mainTaskTableView.getSelectionModel().getSelectedItem() != null){
				 return (Task)this.mainTaskTableView.getSelectionModel().getSelectedItem().clone();				 
			 }else{
				 return null;
			 }
		} catch (CloneNotSupportedException e) {
			return this.mainTaskTableView.getSelectionModel().getSelectedItem();
		}
	 }
	 
	 public TreeViewAccess getSelectedTreeItem(){
		 return this.treeView.getSelectionModel().getSelectedItem().getValue();
	 }
	 
	 public synchronized void setTree(TreeItem<TreeViewAccess> root){
		this.treeView.setRoot(root);
		this.treeView.setShowRoot(false);
		this.settingsButton.setDisable(false);
	 }
	
	 public synchronized List<Team> getTeams(){
		 List<Team> teamList = new ArrayList<>();
		 int i = 0;
		while(this.treeView.getTreeItem(i) != null){
			if(this.treeView.getTreeItem(i).getValue() instanceof Team){
				teamList.add( (Team)this.treeView.getTreeItem(i).getValue() );
			}
			i++;
		}
		return teamList;
	 }
	 
	 public synchronized void generateTree(){
		 new TreeViewCreator().generateTree(this);
	 }
	 
	 public synchronized void generateTasks(int idProject){
		 this.threadPool.submit(new TaskGetThread(this, idProject));
	 }
	 
	 public synchronized void generateMessages(int idTask){
		 this.threadPool.submit(new MessageGetThread(this, idTask));
	 }
}
