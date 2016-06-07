package pl.kielce.tu.kronos.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import pl.kielce.tu.kronos.server.model.ServerProperties;

/**
 * 
 * @author Bartosz Piskiewicz
 *
 */
public class ConsoleReaderThread implements Runnable {
	
	private ServerSocket socket;
	
	public ConsoleReaderThread(ServerSocket socket) {
		this.socket = socket;
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
					System.out.println("Unable to close socket properly. Force stop");
					System.exit(1);
				}
				end = true;
			break;
			case "time":
				System.out.println(new Date().toString());
			break;
			case "threads":
				System.out.println("Active threads: " + Thread.activeCount());
				break;
			case "config":
				System.out.println("Port: " + this.socket.getLocalPort());
				break;
			case "help":
				this.helpMenu();
				break;
			default:
				System.out.println("Error: Command not recognized.");
			}
		}while(!end);
		System.out.println("waiting for killing threads");
//		System.out.println("threads still alive: " + Thread.activeCount());
		System.out.println("Server is shut down.");
		console.close();
	}
	
	private void helpMenu() {
		System.out.println("threads - shows info about number of currently running threads");
		System.out.println("config - informs about port which server is listening on");
		System.out.println("time - prints current time. Just for testing");
		System.out.println("help - shows current describtion");
		System.out.println("exit - shuts down the application");
	}

	public void closeThreads() throws IOException{
		this.socket.close();
	}
}
