package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import View.ClientGUI;

public class btnConnectController_Client extends MouseAdapter {

	ClientGUI clientGUI;

	public btnConnectController_Client(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			String ip = clientGUI.iPaddr.getText().trim();

			if (ip.equals("")) {
				clientGUI.getMainArea().append("Please give an IP address!\n");
				return;
			}

			int portNumber = Integer.parseInt(clientGUI.portNumber.getText().trim());

			if (portNumber < 8000) {
				clientGUI.getMainArea().append("This Port Number must >= 8000, or it maybe occupied\n");
				return;
			}

			String username = clientGUI.txtNickname.getText().trim();
			if (clientGUI.txtNickname.getText().trim().equals("")) {
				clientGUI.getMainArea().append("Please give a username!\n");
				return;
			}

			// System.out.print(String.format("user %s with ip %s, try to
			// connecting to %s", username, ip, portNumber));
			boolean isSuccess = clientGUI.connectServer(portNumber, ip, username);

			if (isSuccess == false) {
				clientGUI.getMainArea().append("ERROR: Failed to Connect to the Server!\n");
				return;
			}

			clientGUI.portNumber.setEnabled(false);
			clientGUI.iPaddr.setEnabled(false);
			clientGUI.txtNickname.setEnabled(false);
			//clientGUI.setTitle(username);
			clientGUI.getMainArea().append("Successfully Connect to the Server!\n");

		} catch (NumberFormatException nfe) {
			clientGUI.getMainArea().append("ERROR: PORT number must be a NUMBER!\n");
		} catch (Exception exception) {
			clientGUI.getMainArea().append("ERROR: Failed to Connect to the Server!\n");
		}

	}
}