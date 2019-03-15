package chatroom;

import java.io.*;
import java.net.*;

public class Server {
	//Server Socket
	private static ServerSocket server = null;
	//Client Socket
	private static Socket client = null;
	
	private static final int maxCapacity = 10;
	private static final ClientThread[] threads = new ClientThread[maxCapacity];
	
	public static void main(String args[]) {
		int portNumber = 3000;
		if(args.length < 1) {
			System.out.println("Now using port number = " + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}
		
		try {
			server = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		while(true) {
			try {
				client = server.accept();
				int i = 0;
				for(i = 0; i < maxCapacity; i++) {
					if(threads[i] == null) {
						(threads[i] = new ClientThread(client, threads)).start();
						break;
					}
				}
				if(i == maxCapacity) {
					PrintStream os = new PrintStream(client.getOutputStream());
					os.println("Multi-Chat Room is currently full. Please visit later!");
					os.close();
					client.close();
				}
			}catch(IOException e) {
				System.out.println(e);
			}
		}
	}
	
}