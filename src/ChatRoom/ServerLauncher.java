package ChatRoom;

import View.ServerGUI;

public class ServerLauncher {

	private static ServerGUI serverGUI;

	public static void main(String[] args) throws Exception {
		try {
			serverGUI = new ServerGUI();
			serverGUI.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
