package pl.kielce.tu.kronos.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeExpansionEvent;

import com.mysql.fabric.xmlrpc.base.Array;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.kielce.tu.kronos.client.model.ServerConnection;
import pl.kielce.tu.kronos.client.tableClasses.Project;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import pl.kielce.tu.kronos.client.threads.TreeViewGeneratorThread;
import pl.kielce.tu.kronos.model.TreeViewAccess;
import javafx.scene.control.cell.TextFieldTreeCell;

/**
 * 
 * @author Dawid Kij Class creates TreeView from objects of
 *         TreeProjectsSerialize
 */
@SuppressWarnings("restriction")
public class TreeViewCreator {
	/**
	 * 
	 * @param tree
	 *            - TreeView handler from FXML Class create TreeView for main
	 *            window
	 */
	public static List<Team> teamList = new ArrayList<>();
	public static List<Project> projectList = new ArrayList<>();
/*
	public void fillTreeView(TreeView<TreeViewAccess> tree) {

//		this.teamList = exampleGenerator();
		Team rootTeam = new Team();
		rootTeam.setName("Root");
		TreeItem<TreeViewAccess> root = new TreeItem<>(rootTeam);
		root.setExpanded(true);
		for (Team x : this.teamList) {
			TreeItem<TreeViewAccess> team;
			team = makeBranch(x, root);
		}
		tree.setRoot(root);
	}*/
	@Deprecated
	public void generateTree(TreeView<TreeViewAccess> tree){
		TreeViewCreator.teamList = Team.getTeams();
		TreeItem<TreeViewAccess> root = new TreeItem<>();	
		root.setExpanded(true);
		for(TreeViewAccess t: TreeViewCreator.teamList){
			TreeItem<TreeViewAccess> teamItem = new TreeItem<>(t);
			root.getChildren().add(teamItem);
			teamItem.setExpanded(false);
			TreeViewCreator.projectList = Project.getProjects(t.getId());
			for(TreeViewAccess p: TreeViewCreator.projectList){
				TreeItem<TreeViewAccess> projectItem = new TreeItem<>(p);
				teamItem.getChildren().add(projectItem);
			}
		}
		tree.setRoot(root);
		tree.setShowRoot(false);
	}
	
	public void generateTree(MainController main){
		main.threadPool.submit(new TreeViewGeneratorThread(main));
	}
/*
	public void fillTreeView2(TreeView<TreeViewAccess> tree) {

		ArrayList<Project> projectList = projectGenerator();
		ArrayList<Team> teamList = teamGenerator();
		pl.kielce.tu.kronos.client.tableClasses.Team.getTeams();

		int j= tree.getExpandedItemCount();// tu jest 3
		for (int i = 0; i < j; i++)
		{
			for (Project x : projectList)// lista wporzadku 5 elementow
			{
				
				int p=0;
				if (x.getIdTeam() == tree.getTreeItem(i).getValue().getId()) 
				{
					tree.getTreeItem(i).setExpanded(true);
					TreeItem<TreeViewAccess> project;
					project = makeBranch2(x, tree.getTreeItem(i));
					System.out.println(tree.getTreeItem(i));
					tree.getTreeItem(i).getChildren().add(project);
					p++;
				}
					//System.out.println(x.getIdTeam());
					//System.out.println(tree.getTreeItem(i).getValue().getIdTeam());
			} 
		}
	}

	/**
	 * Method to help build TreeView
	 * 
	 * @param title
	 *            the name of new TreeItem
	 * @param parent
	 *            - the parent of TreeItem
	 * @return
	 *
	public TreeItem<TreeViewAccess> makeBranch(Team title, TreeItem<TreeViewAccess> parent) {
		TreeItem<TreeViewAccess> item = new TreeItem<>(title);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}

	public TreeItem<TreeViewAccess> makeBranch2(Project title, TreeItem<TreeViewAccess> parent) {
		TreeItem<TreeViewAccess> item = new TreeItem<>(title);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}
*//*
	private static ArrayList<Team> teamGenerator() {

		Team team1 = new Team();
		Team team2 = new Team();
		Team team3 = new Team();
		team1.setIdTeam(1);
		team1.setIdTeamLeader(1);
		team1.setName("Najlepszy");

		team2.setIdTeam(2);
		team2.setIdTeamLeader(2);
		team2.setName("Debesciaki");

		team3.setIdTeam(3);
		team3.setIdTeamLeader(3);
		team3.setName("OPpeople");

		ArrayList<Team> teamList = new ArrayList<>();
		teamList.clear();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);

		return teamList;
	}

	private static ArrayList<Project> projectGenerator() {
		Project project1 = new Project();
		Project project2 = new Project();
		Project project3 = new Project();
		Project project4 = new Project();
		Project project5 = new Project();

		project1.setIdProject(1);
		project1.setIdTeam(1);
		project1.setName("Juwenalia impreza");

		project2.setIdProject(2);
		project2.setIdTeam(1);
		project2.setName("Duze zakupy");

		project3.setIdProject(3);
		project3.setIdTeam(1);
		project3.setName("Projekt Java");

		project4.setIdProject(4);
		project4.setIdTeam(2);
		project4.setName("Stworzenie kampani");

		project5.setIdProject(5);
		project5.setIdTeam(3);
		project5.setName("Przygotowania do ekspansji");

		ArrayList<Project> projectList = new ArrayList<>();
		projectList.clear();
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);
		projectList.add(project4);
		projectList.add(project5);
		return projectList;
	}*/

}
