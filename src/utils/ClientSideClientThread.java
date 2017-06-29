package utils;

import java.io.BufferedReader;
import View.ClientGUI;

public class ClientSideClientThread extends Thread {
	private BufferedReader receiver;
	private ClientGUI clientGUI;
	public String userName;
	
	public ClientSideClientThread(BufferedReader receiver, ClientGUI clientGUI) {
		this.receiver = receiver;
		this.clientGUI = clientGUI;
	}

	public void run() {

		while (true) {

			try {
				if (clientGUI.isConnected) {
					// read message
					String message = receiver.readLine();
					if (message.indexOf("ONLINE:") == 0) {
						String[] onlineUserList = message.split("ONLINE:")[1].split("#");
						clientGUI.names.removeAll(clientGUI.names);
						for (String name : onlineUserList) {
							clientGUI.names.add(name);
						}
						clientGUI.refreshUserList();
					} else {
						if (message.indexOf("USERNAME:") == 0) {
							this.userName = message.split("USERNAME:")[1];
							clientGUI.setTitle(message.split("USERNAME:")[1]);
						}
						// normal message
						String[] info = message == null ? null : message.split("#");
						if (info == null || info[0].equals("SERVER_CLOSE")) {
//							clientGUI.getMainArea().append("The Server is CLOSED! Forced to Offline\n");
							clientGUI.closeConnection(false);
							receiver.close();
							// stop the thread
							return;

						} else {
							clientGUI.getMainArea().append(message + "\n");
						}
					}
				}
			} catch (Exception e) {
				clientGUI.getMainArea().append("The Server is CLOSED! Forced to Offline\n");
				clientGUI.closeConnection(false);
			}
		}
	}

}
