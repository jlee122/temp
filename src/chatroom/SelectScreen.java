package chatroom;

import javax.swing.*;

public class SelectScreen {
	
	public static void main(String[] args) {
		//Allows user to either set up a server or client
		Object[] choices = { "Server", "Client"};
		String default_choice = "Server";
		String welcome_msg = "Login as : ";
		
		Object userSelection = JOptionPane.showInputDialog(null, welcome_msg, "Multi-User ChatRoom", JOptionPane.QUESTION_MESSAGE, null, choices, default_choice);
			
		if(userSelection.equals("Client")) {
			String IPAddress = JOptionPane.showInputDialog("Enter the IP Address:");
			String[] arguments = new String[] {IPAddress};
			new Client().main(arguments);
			
		}else if(userSelection.equals("Server")) {
			String[] arguments = new String[] {};
			new Server().main(arguments);
		}
	}
}