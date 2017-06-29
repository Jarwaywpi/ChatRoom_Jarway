package utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import View.ServerGUI;

public class ServerThread extends Thread {

	ServerGUI serverGUI;
	private List<String> names = new ArrayList<String>();
	private ServerSocket serverSocket;
	private ArrayList<ServerSideClientThreads> serverSideClientThreads;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public ArrayList<ServerSideClientThreads> getServerSideClientThreads() {
		return serverSideClientThreads;
	}

	public void setServerSideClientThreads(ArrayList<ServerSideClientThreads> serverSideClientThreads) {
		this.serverSideClientThreads = serverSideClientThreads;
	}

	public ServerThread(ServerSocket socket, ArrayList<ServerSideClientThreads> serverSideClientThreads,
			ServerGUI serverGUI) {
		this.serverSocket = socket;
		this.serverSideClientThreads = serverSideClientThreads;
		this.serverGUI = serverGUI;
	}

	public void run() {

		while (true) {

			try {
				Socket socket = serverSocket.accept();
				ServerSideClientThreads client = new ServerSideClientThreads(socket, serverGUI);
				client.start();

				String newName = client.userName;
				
				if (names.contains(newName)) {
					int charPointer = (int)(Math.random() * (newName.length() - 1));
					char a = newName.charAt(charPointer);
					String b = String.valueOf(a);
					String[] splitedNewName = newName.split(b);
					if (splitedNewName.length >= 3) {
						for (int i = 2; i < splitedNewName.length; i++) {
							splitedNewName[1] += splitedNewName[i];
						}
					}
					newName = splitedNewName[0] + String.valueOf((int) (Math.random() * 101)) + b + splitedNewName[1];
				}
				client.setName(newName);
				client.userName = newName;
				ConnectingUtils.sendMessageToSocket("USERNAME:" + newName, client.getWriter());
				names.add(newName);
				String onlineUserList = "ONLINE:";
				for (String name : names) {
					onlineUserList += name + "#";
				}
				ConnectingUtils.sendMessageToSocket(onlineUserList, client.getWriter());
				for (ServerSideClientThreads thread : serverGUI.serverThread.getServerSideClientThreads()) {
					ConnectingUtils.sendMessageToSocket(onlineUserList, thread.getWriter());
					ConnectingUtils.sendMessageToSocket("USER#" + newName + " is ONLINE!\n", thread.getWriter());
				}
				serverSideClientThreads.add(client);
				serverGUI.mainArea.append(newName + " is Online!\n");
				serverGUI.refreshUserList();

			} catch (IOException e) {
				serverGUI.mainArea.append("ERROR: The server thread does not work correctly!\n");
				serverGUI.mainArea.append(e.getMessage());
				e.printStackTrace();
			}

		}

	}
}
