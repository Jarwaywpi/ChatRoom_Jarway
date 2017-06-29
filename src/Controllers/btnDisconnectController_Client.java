package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import View.ClientGUI;

public class btnDisconnectController_Client extends MouseAdapter {

	ClientGUI clientGUI;

	public btnDisconnectController_Client(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clientGUI.clientList.setText("");
		clientGUI.closeConnection(true);
		// ConnectingUtils.sendMessageToSocket("OFFLINE"+clientGUI.getTitle(),
		// clientGUI.sender);
		clientGUI.getMainArea().append("Successfuly Disconnect from the Server!\n");

		clientGUI.portNumber.setEnabled(true);
		clientGUI.iPaddr.setEnabled(true);
		clientGUI.txtNickname.setEnabled(true);
		System.exit(0);
	}
}
