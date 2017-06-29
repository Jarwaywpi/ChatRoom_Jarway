package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import View.ServerGUI;

public class ServerSideClientThreads extends Thread {
	private Socket socket;
	private BufferedReader receiver;
	private PrintWriter sender;
	public String userName;
	ServerGUI serverGUI;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setReader(BufferedReader receiver) {
		this.receiver = receiver;
	}

	public void setWriter(PrintWriter sender) {
		this.sender = sender;
	}

	public BufferedReader getReader() {
		return receiver;
	}

	public PrintWriter getWriter() {
		return sender;
	}

	public ServerSideClientThreads(Socket socket, ServerGUI serverGUI) {

		try {

			this.socket = socket;
			this.receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.sender = new PrintWriter(socket.getOutputStream());
			this.serverGUI = serverGUI;

			String[] userInfo = receiver.readLine().split("#");
			userName = String.valueOf(userInfo[0]);
			ConnectingUtils.sendMessageToSocket(
					"Server Response: " + userName + "Connect to the Server Successfully", sender);

		} catch (IOException e) {
			serverGUI.mainArea.append("ERROR: Something Wrong when the server creates a thread for the Client\n");
			serverGUI.mainArea.append(e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public void run() {
		String message = null;
		while (true) {
			try {
				message = receiver.readLine();
				if (message.indexOf("OFFLINE") == 0) {
					String[] info = message.split("OFFLINE");
					serverGUI.serverThread.getServerSideClientThreads().remove(this);
					serverGUI.serverThread.getNames().remove(info[1]);
					serverGUI.refreshUserList();
					String onlineUserList = "ONLINE:";
					for (String name : serverGUI.serverThread.getNames()) {
						onlineUserList += name + "#";
					}
					ConnectingUtils.releaseResource(receiver, sender, socket, serverGUI.mainArea);
					serverGUI.mainArea.append("Client #" + info[1] + "Off line, now there's "
							+ serverGUI.serverThread.getServerSideClientThreads().size() + " Online!");
					if (serverGUI.serverThread.getServerSideClientThreads() != null) {
						for (ServerSideClientThreads thread : serverGUI.serverThread.getServerSideClientThreads()) {
							ConnectingUtils.sendMessageToSocket(info[1] + " is Offline!", thread.getWriter());
							// renew OnlineUserList
							ConnectingUtils.sendMessageToSocket(onlineUserList, thread.getWriter());
						}
					}
				} else {
					String[] info = message.split("#");
					serverGUI.mainArea.append(info[0] + " says: " + info[1] + "\n");
					for (ServerSideClientThreads thread : serverGUI.serverThread.getServerSideClientThreads()) {
						ConnectingUtils.sendMessageToSocket(info[0] + " says: " + info[1], thread.getWriter());
					}
				}
			} catch (IOException e) {
				ConnectingUtils.releaseResource(receiver, sender, socket, serverGUI.mainArea);
			} catch (NullPointerException ne) {

			}
		}
	}
}
