package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import Controllers.btnConnectController_Server;
import Controllers.btnDisconnectController_Server;
import Controllers.btnSendMessageController_Server;
import utils.ConnectingUtils;
import utils.ServerSideClientThreads;
import utils.ServerThread;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class ServerGUI extends JFrame {

	private ServerSocket serverSocket;
	public ServerThread serverThread;
	// Record all the server threads that serve the clients
	private static ArrayList<ServerSideClientThreads> serverSideClientThreads;
	public boolean isRunning;
	
	private JPanel serverGUIPane;
	public JTextField portNumber;
	public JTextField messageInput;
	public JButton btnSendMessage;
	public JButton btnDisconnect;
	public JButton btnConnect;
	public JTextArea mainArea;
	private JScrollPane scrollPane_MessageArea;
	private JScrollPane scrollPane_UserList;
	private JTextArea clientList;

	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		
		setTitle("SERVER");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		serverGUIPane = new JPanel();
		serverGUIPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(serverGUIPane);
		serverGUIPane.setLayout(null);
		
		scrollPane_MessageArea = new JScrollPane();
		scrollPane_MessageArea.setBounds(6, 6, 350, 230);
		serverGUIPane.add(scrollPane_MessageArea);

		mainArea = new JTextArea();
		scrollPane_MessageArea.setViewportView(mainArea);
		
		portNumber = new JTextField();
		portNumber.setText("8080");
		portNumber.setBounds(368, 169, 126, 28);
		serverGUIPane.add(portNumber);
		portNumber.setColumns(10);
		
		scrollPane_UserList = new JScrollPane();
		scrollPane_UserList.setBounds(368, 6, 126, 163);
		serverGUIPane.add(scrollPane_UserList);

		clientList = new JTextArea();
		scrollPane_UserList.setViewportView(clientList);
		clientList.setEditable(false);

		messageInput = new JTextField();
		messageInput.setBounds(6, 244, 245, 28);
		serverGUIPane.add(messageInput);
		messageInput.setColumns(10);

		btnSendMessage = new JButton("Send");
		btnSendMessage.addMouseListener(new btnSendMessageController_Server(this));
		btnSendMessage.setBounds(249, 245, 117, 29);
		serverGUIPane.add(btnSendMessage);

		btnDisconnect = new JButton("DISCONNECT");
		btnDisconnect.addMouseListener(new btnDisconnectController_Server(this));
		btnDisconnect.setBounds(368, 233, 126, 39);
		serverGUIPane.add(btnDisconnect);

		btnConnect = new JButton("CONNECT");
		btnConnect.addMouseListener(new btnConnectController_Server(this));
		btnConnect.setBounds(368, 197, 126, 39);
		serverGUIPane.add(btnConnect);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (true) {
					// closeServer();
				}
				System.exit(0);
			}
		});
		
		btnDisconnect.setEnabled(false);
		btnSendMessage.setEnabled(false);
	}
	
	public void startServer(int port) {
		try {
			// get a new arrayList for online users
			serverSideClientThreads = new ArrayList<ServerSideClientThreads>();
			serverSocket = new ServerSocket(port);
			// start a new server thread
//			this.mainArea.append("INFO: Socket is here\n");
			serverThread = new ServerThread(serverSocket, serverSideClientThreads, this);
			serverThread.start();
			isRunning = true;
//			this.mainArea.append(isRunning?"It's Running":"It's not");
		} catch (Exception exception) {
			isRunning = false;
			this.mainArea.append("ERROR: Server did not start correctlly!\n");
		}
	}
	
	public void closeServer() {
		try {
			if (!isRunning) {
				//end the server thread
				this.serverThread.getNames().clear();
				refreshUserList();
				// tell all the user that the server is stopped
				for(ServerSideClientThreads thread : serverThread.getServerSideClientThreads()){
					ConnectingUtils.sendMessageToSocket("SERVER_CLOSE", thread.getWriter());
					ConnectingUtils.releaseResource(thread.getReader(), thread.getWriter(), thread.getSocket(), this.mainArea);	
				}
				serverSideClientThreads.clear();
				if (!serverSocket.isClosed()) {
					serverSocket.close();
				}
				this.serverThread.stop();
				isRunning = false;
			}
		} catch (IOException e) {
			this.mainArea.append("ERROR: Something wrong happened when close the Server!\n");
			this.mainArea.append(e.getMessage());
			isRunning = true;
		}
	}
	
	public void refreshUserList() {
		clientList.setText("");
		for (String name : serverThread.getNames()) {
			clientList.append(name + "\n");
		}
	}
}
