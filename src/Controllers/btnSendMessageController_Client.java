package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import View.ClientGUI;
import utils.ConnectingUtils;

public class btnSendMessageController_Client extends MouseAdapter {

	ClientGUI clientGUI;

	public btnSendMessageController_Client(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!clientGUI.isConnected) {
			clientGUI.getMainArea().append("ERROR: You need to connect to one Server First!\n");
			return;
		}
		// send message to server
		ConnectingUtils.sendMessageToSocket(clientGUI.clientThread.userName + "#" + clientGUI.messageInput.getText().trim(),
				clientGUI.sender);
		clientGUI.messageInput.setText("");
	}

}