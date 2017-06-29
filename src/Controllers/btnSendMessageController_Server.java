package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import View.ServerGUI;
import utils.ConnectingUtils;
import utils.ServerSideClientThreads;

public class btnSendMessageController_Server extends MouseAdapter {

	ServerGUI serverGUI;

	public btnSendMessageController_Server(ServerGUI serverGUI) {
		this.serverGUI = serverGUI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		String message = serverGUI.messageInput.getText();
		if (!message.isEmpty()) {
			if (!serverGUI.isRunning) {
				serverGUI.mainArea.append("ERROR: The server is not running yet, cannot send message!\n");
				return;
			}

			if (serverGUI.serverThread.getServerSideClientThreads().size() == 0) {
				serverGUI.mainArea.append("ERROR: No Online user, cannot send message!\n");
				return;
			}
			List<String> isWhisper = new ArrayList<String>();
			for (String name : serverGUI.serverThread.getNames()) {
				if (message.indexOf("@"+name) != -1) {
					isWhisper.add(name);
				}
			}
			for(ServerSideClientThreads thread : serverGUI.serverThread.getServerSideClientThreads()){
				if (isWhisper.isEmpty()) {
					ConnectingUtils.sendMessageToSocket(("Server Msg: " + message), thread.getWriter());
				} else if (isWhisper.contains(thread.userName)) {
					ConnectingUtils.sendMessageToSocket(("Server Msg: " + message), thread.getWriter());
				}
			}
			serverGUI.mainArea.append(message + "\n");
			serverGUI.messageInput.setText("");
		}
	}
}
