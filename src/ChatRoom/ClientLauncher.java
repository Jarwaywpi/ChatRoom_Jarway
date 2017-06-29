package ChatRoom;

import View.ClientGUI;

public class ClientLauncher {

	private static ClientGUI frame;

	public static void main(String[] args) throws Exception {
		try {
			frame = new ClientGUI();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
