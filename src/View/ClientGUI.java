package View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controllers.btnConnectController_Client;
import Controllers.btnDisconnectController_Client;
import Controllers.btnSendMessageController_Client;
import utils.ClientSideClientThread;
import utils.ConnectingUtils;
import javax.swing.JScrollPane;

public class ClientGUI extends JFrame {

	private JPanel clientGUIPane;

	public JTextField portNumber;
	public JTextField messageInput;
	public JTextField iPaddr;
	public JTextField txtNickname;
	private JTextArea mainArea;
	private JButton btnConnect;
	private JButton btnDisconnect;
	public JTextArea clientList;
	public JButton btnSendMessage;

	public boolean isConnected = false;
	private Socket socket;
	public PrintWriter sender;
	private BufferedReader receiver;
	public ClientSideClientThread clientThread;

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		setTitle("CLIENT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		clientGUIPane = new JPanel();
		clientGUIPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(clientGUIPane);
		clientGUIPane.setLayout(null);

		scrollPane_MessageArea = new JScrollPane();
		scrollPane_MessageArea.setBounds(6, 6, 350, 230);
		clientGUIPane.add(scrollPane_MessageArea);

		mainArea = new JTextArea();
		scrollPane_MessageArea.setViewportView(mainArea);

		portNumber = new JTextField();
		portNumber.setText("8080");
		portNumber.setBounds(368, 136, 126, 28);
		clientGUIPane.add(portNumber);
		portNumber.setColumns(10);

		scrollPane_USERLIST = new JScrollPane();
		scrollPane_USERLIST.setBounds(368, 6, 126, 130);
		clientGUIPane.add(scrollPane_USERLIST);

		clientList = new JTextArea();
		clientList.setEditable(false);
		scrollPane_USERLIST.setViewportView(clientList);

		messageInput = new JTextField();
		messageInput.setBounds(6, 244, 172, 28);
		clientGUIPane.add(messageInput);
		messageInput.setColumns(10);

		btnSendMessage = new JButton("Send");
		btnSendMessage.addMouseListener(new btnSendMessageController_Client(this));
		btnSendMessage.setBounds(175, 245, 75, 29);
		clientGUIPane.add(btnSendMessage);

		btnDisconnect = new JButton("DISCONNECT");
		btnDisconnect.addMouseListener(new btnDisconnectController_Client(this));
		btnDisconnect.setBounds(244, 245, 112, 28);
		clientGUIPane.add(btnDisconnect);

		btnConnect = new JButton("CONNECT");
		btnConnect.addMouseListener(new btnConnectController_Client(this));
		btnConnect.setBounds(368, 225, 126, 48);
		clientGUIPane.add(btnConnect);

		iPaddr = new JTextField();
		iPaddr.setText("127.0.0.1");
		iPaddr.setColumns(10);
		iPaddr.setBounds(368, 167, 126, 28);
		clientGUIPane.add(iPaddr);

		txtNickname = new JTextField();
		txtNickname.setText("NickName");
		txtNickname.setColumns(10);
		txtNickname.setBounds(368, 196, 126, 28);
		clientGUIPane.add(txtNickname);

		btnDisconnect.setEnabled(false);
		btnSendMessage.setEnabled(false);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (true) {
					closeConnection(true);
				}
				System.exit(0);
			}
		});
		
	}
	

	public JTextArea getMainArea() {
		return mainArea;
	}

	public boolean closeConnection(boolean flag) {
		this.names = new ArrayList<String>();
		refreshUserList();
		if (flag) {
			ConnectingUtils.sendMessageToSocket("OFFLINE" + this.getTitle(), sender);
			// release all the resource
			ConnectingUtils.releaseResource(receiver, sender, socket, mainArea);
			isConnected = false;
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);
			btnSendMessage.setEnabled(false);
			return true;
		} else {
			ConnectingUtils.releaseResource(receiver, sender, socket, mainArea);

			portNumber.setEnabled(true);
			iPaddr.setEnabled(true);
			txtNickname.setEnabled(true);
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);
			btnSendMessage.setEnabled(false);
			isConnected = false;

			return true;
		}

	}

	public List<String> names = new ArrayList<String>();
	private JScrollPane scrollPane_MessageArea;
	private JScrollPane scrollPane_USERLIST;

	public boolean connectServer(int port, String ipAdd, String name) {
		try {
			System.out.print("get a new connection");
			this.socket = new Socket(ipAdd, port);
			System.out.print("socket");
			this.sender = new PrintWriter(socket.getOutputStream());
			this.receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// write user information into socket
			ConnectingUtils.sendMessageToSocket(name + "#" + "/" + ipAdd, sender);
			// get a thread that listen to the response from the server
			clientThread = new ClientSideClientThread(receiver, this);
			clientThread.start();
			isConnected = true;

			btnConnect.setEnabled(false);
			btnDisconnect.setEnabled(true);
			btnSendMessage.setEnabled(true);
			return true;

		} catch (Exception e) {
			isConnected = false;
			return false;
		}
	}

	public void refreshUserList() {
		clientList.setText("");
		for (String name : names) {
			clientList.append(name + "\n");
		}
		this.setTitle(clientThread.userName);
	}

}
