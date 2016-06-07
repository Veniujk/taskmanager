package pl.kielce.tu.kronos.server;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class OldConsoleReaderThread implements Runnable {
	
	private ExecutorService threadPool;
	private Thread mainThread;
//	private Selector selector;
	
	public OldConsoleReaderThread(ExecutorService threadPool, Thread mainThread) {
		super();
		this.threadPool = threadPool;
		this.mainThread = mainThread;
	}

	@Override
	public void run() {
		Scanner console = new Scanner(System.in);
		boolean end = false;
		do{
			System.out.print("task-manager-server > ");
			String command = console.nextLine();
			switch(command){
			case "exit":
				System.out.println("Trying to shut down");
				try{
					this.closeThreads();
				}catch(IOException ex){
					System.out.println("Unable to close selector properly. Force stop");
					System.exit(1);
				}
				end = true;
			break;
			case "time":
				System.out.println(new Date().toString());
			break;
			case "status":
				System.out.println("bangla");
			break;
			default:
				System.out.println("Error: Command not recognized.");
			}
		}while(!end);
		while(Thread.activeCount()>1){}
		System.out.println("Server is shut down.");
		console.close();
	}
	
	public void closeThreads() throws IOException{
		this.mainThread.interrupt();
		this.threadPool.shutdownNow();
	}

}
