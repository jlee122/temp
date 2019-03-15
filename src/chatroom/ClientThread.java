package chatroom;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
	private String userName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket user = null;
	private final ClientThread[] threads;
	private int maxCapacity;
	
	public ClientThread(Socket userSocket, ClientThread[] threads) {
		this.user = userSocket;
		this.threads = threads;
		maxCapacity = threads.length;
	}
	
	public void run() {
		int maxCapacity = this.maxCapacity;
		ClientThread[] threads = this.threads;
		System.out.println("hello");
		try {
			is = new DataInputStream(user.getInputStream());
			os = new PrintStream(user.getOutputStream());
			String name;
			while(true) {
				os.println("Enter your name that will be appeared as in chatroom: ");
				name = is.readLine().trim();
				if(name.indexOf('@') == -1) {
					break;
				}else {
					os.println("The name cannot contain '@' character. ");
				}
			}
			
			// Welcome User
			os.println("Welcome " + name + " to our multi-user chat room.\\nTo leave enter /quit in a new line.");
			synchronized(this) {
				for(int i = 0; i < maxCapacity; i++) {
					if(threads[i] != null && threads[i] == this) {
						userName = "@" + name;
						break;
					}
				}
				for(int j = 0; j < maxCapacity; j++) {
					if (threads[j] != null && threads[j] != this) {
			            threads[j].os.println("-------- A new user " + name
			                + " entered the chat room !!! --------");
			          }
				}
			}
			while(true) {
				String line = is.readLine();
				if(line.startsWith("/quit")) {
					break;
				}
				synchronized(this) {
					for(int i = 0; i < maxCapacity; i++) {
						if(threads[i] != null && threads[i].userName != null) {
							threads[i].os.println("<" + name + "> " + line);
						}
					}
				}
			}
		synchronized(this) {
			for(int i= 0; i < maxCapacity; i++) {
				if(threads[i] != null && threads[i] != this && threads[i].userName != null) {
					threads[i].os.println("-------- The user " + name + "is leaving the chat room... --------");
				}
			}
		}
		os.println("-------- Good Bye " + name + " --------");
		
		synchronized(this) {
			for(int i = 0; i < maxCapacity; i++) {
				if(threads[i] == this) {
					threads[i] = null;
				}
			}
		}
		
		is.close();
		os.close();
		user.close();
	}catch(IOException e) {
		
	}
	}
}
