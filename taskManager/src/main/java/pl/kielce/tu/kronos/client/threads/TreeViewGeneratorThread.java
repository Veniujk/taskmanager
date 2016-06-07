package pl.kielce.tu.kronos.client.threads;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.TreeViewCreator;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import pl.kielce.tu.kronos.model.TreeViewAccess;

public class TreeViewGeneratorThread implements Runnable{

	private MainController main;
	
	public TreeViewGeneratorThread(MainController main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		List<Team> teamList = Team.getTeams();
		TreeItem<TreeViewAccess> root = new TreeItem<>();	
		root.setExpanded(true);
		for(TreeViewAccess t: teamList){
			TreeItem<TreeViewAccess> teamItem = new TreeItem<>(t);
			root.getChildren().add(teamItem);
			teamItem.setExpanded(true);
			List<Project> projectList = Project.getProjects(t.getId());
			for(TreeViewAccess p: projectList){
				TreeItem<TreeViewAccess> projectItem = new TreeItem<>(p);
				teamItem.getChildren().add(projectItem);
			}
		}
		Platform.runLater(() -> this.main.setTree(root));
	}
}
