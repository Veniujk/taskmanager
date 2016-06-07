package pl.kielce.tu.kronos.client.daemons;

import java.util.Date;

import pl.kielce.tu.kronos.client.MainController;

public class TreeViewUpdateDaemon implements Runnable{

	private final int REFRESH_SECONDS_INTERVAL;
	private MainController main;
	
	public TreeViewUpdateDaemon(MainController main) {
		this.main = main;
		this.REFRESH_SECONDS_INTERVAL = 120;
	}

	@Override
	public void run() {
		for(;;){
			try {
				Thread.sleep(REFRESH_SECONDS_INTERVAL * 1000);
				this.main.generateTree();
			} catch (InterruptedException e) {
			}
		}
	}
}
