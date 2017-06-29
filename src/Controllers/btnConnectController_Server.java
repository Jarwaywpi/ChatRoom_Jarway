package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import View.ServerGUI;
import ChatRoom.ServerLauncher;

public class btnConnectController_Server extends MouseAdapter {

	ServerGUI serverGUI;

	public btnConnectController_Server(ServerGUI serverGUI) {
		this.serverGUI = serverGUI;
	}

	/*
	 * this is the mouse event for Server's connect button. which validate the
	 * Port, and then create a new Server thread
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			int portNumber = Integer.parseInt(serverGUI.portNumber.getText());
			if (portNumber < 8000) {
				serverGUI.mainArea.append("This port Number is inValid!\n");
			} else {
				serverGUI.startServer(portNumber);
				serverGUI.portNumber.setEnabled(false);
				serverGUI.btnConnect.setEnabled(false);
				serverGUI.btnDisconnect.setEnabled(true);
				serverGUI.btnSendMessage.setEnabled(true);
				serverGUI.mainArea.append(String.format("The Server on port %s starts successfully!\n", portNumber));
			}
		} catch (NumberFormatException ne) {
			serverGUI.mainArea.append("Please input a valid port number. (>8000)\n");
		}
	}
}
