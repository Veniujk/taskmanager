package pl.kielce.tu.kronos.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kielce.tu.kronos.model.Request;
import pl.kielce.tu.kronos.server.model.CommandLineParser;
import pl.kielce.tu.kronos.server.model.DatabaseCredencialsException;
import pl.kielce.tu.kronos.server.model.DatabaseProperties;
import pl.kielce.tu.kronos.server.model.RequestThread;
import pl.kielce.tu.kronos.server.model.ServerProperties;

public class Server {
	
	public static ExecutorService threadPool;
	public static ServerSocket server;
	private static List<Socket> socketList;
	public static List<RequestThread> threadList;
	public static TableStateWatcher recorder = new TableStateWatcher();
	/**
	 * Main method of server
	 * @author Bartosz Piskiewicz
	 * @param args Optional [-dpa] db-ip::db-port::db-user-db-pass::db-database::jdbc-driver::jdbc-connection port admin-password
	 */
	public static void main(String[] args) {
		System.out.println("server start");
		System.out.println(System.getProperty("user.dir"));
		if(args.length > 0){
			try {
				new CommandLineParser(args);
			} catch (IOException e) {
				System.err.println("Unable to update config files");
				e.printStackTrace();
				System.exit(1);
			}
		}
		threadPool = Executors.newFixedThreadPool(5);
		socketList = new LinkedList<>();
		threadList = new LinkedList<>();
		try(ServerSocket server = new ServerSocket(new ServerProperties().getPort());){
			Server.server = server;
			Runnable consoleReader = new ConsoleReaderThread(server);
			Thread consoleThread = new Thread(consoleReader);
			consoleThread.start();
			while(true){
				Socket s = server.accept(); 
				socketList.add(s);
				RequestThread thread = new RequestThread(s);
				threadPool.submit(thread);
				threadList.add(thread);
			}
			
		}catch(SocketException e){
			System.out.println("main socket closed");
			
		}catch(IOException ex){
			ex.printStackTrace();
			System.out.println("Unable to create socket");
			System.exit(1);
		}finally{
			for(Socket s: socketList){
				try{
					s.close();
				}catch(IOException ex){
					System.out.println("unbable to close socket");
				}	
			}
		}
		socketList.clear();
		threadList.clear();
		Server.threadPool.shutdown();
//		System.gc();
		System.out.println("main thread killed");
	}	
	/*
	public static void runSelector(){
		try(Selector socketSelector = Selector.open(); ServerSocketChannel serverSocket = ServerSocketChannel.open()){
			serverSocket.bind(new InetSocketAddress(new ServerProperties().getPort()));
			serverSocket.configureBlocking(false);
			serverSocket.register(socketSelector, SelectionKey.OP_ACCEPT);
			System.out.println("selector is fine");
			while(!Thread.currentThread().isInterrupted()){
				int numberOfSelected = socketSelector.select(100);
				if(numberOfSelected > 0){
//					Server.serveSelection(socketSelector);
					System.out.println(numberOfSelected);
					System.out.println("theres request");
					Set<SelectionKey> selectedKeys = socketSelector.selectedKeys();
					Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
					while(keyIterator.hasNext()){
						SelectionKey key = keyIterator.next();
						if(key.isAcceptable()){
							System.out.println("it is acceptable");
							SocketChannel newSocketChannel = serverSocket.accept();
							newSocketChannel.configureBlocking(false);
							newSocketChannel.register(socketSelector, SelectionKey.OP_READ);
						}else if(key.isReadable()){
							System.out.println("it is readable");
							Socket socket = ((SocketChannel)key.channel()).socket();
							ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
							System.out.println(((Request)in.readObject()).getJSONdata());
							in.close();
					//		Socket s = ((SocketChannel))key.channel()).s;
//							Server.threadPool.submit(new RequestThread(key));
						}
//					key.cancel();
//						keyIterator.remove();
					}
				selectedKeys.clear();
				}
			}
			System.out.println("out of loop");
		}catch(IOException | ClassNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	private static void serveSelection(Selector socketSelector){
		System.out.println("request incoming");
		Set<SelectionKey> selectedKeys = socketSelector.selectedKeys();
		Iterator<SelectionKey> iterator = selectedKeys.iterator();
		while(iterator.hasNext()){
			SelectionKey currentKey = iterator.next();
			if(currentKey.isAcceptable()){
				System.out.println("accept it");
//				SocketChannel newSocketChannel = serverSocket.accept();
//				newSocketChannel.configureBlocking(false);
//				newSocketChannel.register(socketSelector, SelectionKey.OP_READ);
			}
			if(currentKey.isReadable()){
				System.out.println("reading stuff");
			}
			currentKey.cancel();
		}
		
	}*/
	
}
