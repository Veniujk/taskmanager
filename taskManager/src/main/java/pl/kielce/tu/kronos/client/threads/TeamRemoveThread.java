package pl.kielce.tu.kronos.client.threads;
import javafx.application.Platform;
import pl.kielce.tu.kronos.client.MainController;
import pl.kielce.tu.kronos.client.SettingsController;
import pl.kielce.tu.kronos.client.tableClasses.Team;
import pl.kielce.tu.kronos.client.TreeViewCreator;
public class TeamRemoveThread implements Runnable {
	SettingsController controler;
	MainController main;
	int idTeam;
	public TeamRemoveThread(SettingsController controler, MainController main, int idTeam) {
		super();
		this.controler = controler;
		this.main = main;
		this.idTeam = idTeam;
	}
	@Override
	public void run() {
		Team.removeTeam(this.idTeam);
		Platform.runLater(()->new TreeViewCreator().generateTree(main));
		Platform.runLater(()->this.controler.enableButtonDeleteTeam());
		Platform.runLater(()->this.controler.teamChoiceBoxesRefresh());
		
	}
	

}
