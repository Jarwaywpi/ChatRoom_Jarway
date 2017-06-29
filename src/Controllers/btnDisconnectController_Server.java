package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import View.ServerGUI;

public class btnDisconnectController_Server extends MouseAdapter {

	ServerGUI serverGUI;

	public btnDisconnectController_Server(ServerGUI serverGUI) {
		this.serverGUI = serverGUI;
	}

	/*
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		serverGUI.closeServer();
		serverGUI.portNumber.setEnabled(true);
		serverGUI.btnConnect.setEnabled(true);
		serverGUI.btnDisconnect.setEnabled(false);
		serverGUI.btnSendMessage.setEnabled(false);
		serverGUI.mainArea.append("Successfully end this server!\n");
		System.exit(0);
	}
}
