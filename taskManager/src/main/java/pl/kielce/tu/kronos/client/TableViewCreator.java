package pl.kielce.tu.kronos.client;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.kielce.tu.kronos.client.tableClasses.*;
/**
 * 
 * @author Dawid Kij
 *Class used to create TableView for main window
 */
@SuppressWarnings("restriction")
public class TableViewCreator {
	List<Task> taskList = new ArrayList<>();



	/**
	  * 
	  * @param main - TableView handler from FXML
	  * Class create ObservableList for TaskTable in main window
	  */
/* public void fillTableView(TableView<Task> main)
		{
		 Calendar calendar = Calendar.getInstance();
		 
		 LocalDateTime data1 = LocalDateTime.of(2016,8,05,16,26,4);
		 LocalDateTime data11 = LocalDateTime.of(2016,8,05,16,26,6);
		 calendar.set(2016,9,05,16,26,6);
		 Date data1D = new Date();
		 data1D = calendar.getTime();
		 
		 LocalDateTime data2 = LocalDateTime.of(2016,8,05,16,26,6);
		 LocalDateTime data22 = LocalDateTime.of(2016,8,05,16,26,6);	 
		 calendar.set(2016,10,05,16,26,6);
		 Date data2D = new Date();
		 data2D = calendar.getTime();
		 
		 
		 
		 
			ObservableList<Task> data = FXCollections.observableArrayList();
			data.addAll(
					new Task(1,"zrobic_projekt","aktywne",data1,data11,data1D,1,1),
					new Task(2,"zrobic_GUI","skonczone",data2,data22,data2D,2,2));
		
			
			
			TableColumn<Task, String> nameColumn = new TableColumn<Task, String>("Nazwa");
			nameColumn.setMinWidth(150);
			nameColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("name"));
			
			TableColumn<Task, String> statusColumn = new TableColumn<Task, String>("Status");
			statusColumn.setMinWidth(75);
			statusColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
			
			TableColumn<Task, String> personColumn = new TableColumn<Task, String>("Zlecone");
			personColumn.setMinWidth(100);
			personColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("id"));
			
			TableColumn<Task, LocalDateTime> createdColumn = new TableColumn<Task, LocalDateTime>("Dodano");
			createdColumn.setMinWidth(101);
			createdColumn.setCellValueFactory(new PropertyValueFactory<Task,LocalDateTime>("createdAt"));
			
			TableColumn<Task, LocalDateTime> updatedColumn = new TableColumn<Task, LocalDateTime>("Zmieniono");
			updatedColumn.setMinWidth(101);
			updatedColumn.setCellValueFactory(new PropertyValueFactory<Task,LocalDateTime>("updatedAt"));
			
			TableColumn<Task, Date> deadline = new TableColumn<Task, Date>("Deadline");
			deadline.setMinWidth(101);
			deadline.setCellValueFactory(new PropertyValueFactory<Task,Date>("deadline"));
			
			main.setItems(data);
			main.getColumns().addAll(nameColumn,statusColumn,personColumn,createdColumn,updatedColumn,deadline);
			main.getSelectionModel().select(0);
			
		}*/
	 public void generateTaskTable(TableView<Task> main)
	 {
		main.getColumns().clear();
		 ObservableList<Task> data  = FXCollections.observableArrayList();
//		 data.addAll(taskList);
		 TableColumn<Task, String> nameColumn = new TableColumn<Task, String>("Nazwa");
			nameColumn.setMinWidth(250);
			nameColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("name"));
			
			TableColumn<Task, String> statusColumn = new TableColumn<Task, String>("Status");
			statusColumn.setMinWidth(50);
			statusColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
			
			TableColumn<Task, String> personColumn = new TableColumn<Task, String>("Zlecone");
			personColumn.setMinWidth(100);
			personColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("userName"));
			
			TableColumn<Task, Date> createdColumn = new TableColumn<Task, Date>("Dodano");
			createdColumn.setMinWidth(101);
			createdColumn.setCellValueFactory(new PropertyValueFactory<Task,Date>("createdAt"));
			
			TableColumn<Task, Date> updatedColumn = new TableColumn<Task, Date>("Zmieniono");
			updatedColumn.setMinWidth(101);
			updatedColumn.setCellValueFactory(new PropertyValueFactory<Task,Date>("updatedAt"));
			
			TableColumn<Task, Date> deadline = new TableColumn<Task, Date>("Deadline");
			deadline.setMinWidth(101);
			deadline.setCellValueFactory(new PropertyValueFactory<Task,Date>("deadline"));
			
			main.setItems(data);
			main.getColumns().addAll(nameColumn,statusColumn,personColumn,createdColumn,updatedColumn,deadline);
			main.getSelectionModel().clearSelection();
	 }
	
	public void generateTaskTable(TableView<Task> main, List<Task> taskList){
		 main.getColumns().clear();
		 ObservableList<Task> data  = FXCollections.observableArrayList();
		 data.addAll(taskList);
		 TableColumn<Task, String> nameColumn = new TableColumn<Task, String>("Nazwa");
			nameColumn.setMinWidth(250);
			nameColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("name"));
			
			TableColumn<Task, String> statusColumn = new TableColumn<Task, String>("Status");
			statusColumn.setMinWidth(50);
			statusColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
			
			TableColumn<Task, String> personColumn = new TableColumn<Task, String>("Zlecone");
			personColumn.setMinWidth(100);
			personColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("userName"));
			
			TableColumn<Task, Date> createdColumn = new TableColumn<Task, Date>("Dodano");
			createdColumn.setMinWidth(101);
			createdColumn.setCellValueFactory(new PropertyValueFactory<Task,Date>("createdAt"));
			
			TableColumn<Task, Date> updatedColumn = new TableColumn<Task, Date>("Zmieniono");
			updatedColumn.setMinWidth(101);
			updatedColumn.setCellValueFactory(new PropertyValueFactory<Task,Date>("updatedAt"));
			
			TableColumn<Task, Date> deadline = new TableColumn<Task, Date>("Deadline");
			deadline.setMinWidth(101);
			deadline.setCellValueFactory(new PropertyValueFactory<Task,Date>("deadline"));
			
			main.setItems(data);
			main.getColumns().addAll(nameColumn,statusColumn,personColumn,createdColumn,updatedColumn,deadline);
//			main.getSelectionModel().clearSelection();
	 }
}
