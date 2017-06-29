package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

/*
 * This is the class for the connecting of ServerThread and ClientThreads
 */
public class ConnectingUtils {

	public static void sendMessageToSocket(String message, PrintWriter sender) {
		sender.println(message);
		sender.flush();
	}

	public static boolean releaseResource(BufferedReader reader, PrintWriter writer, Socket socket,
			JTextArea messageField) {

		boolean Done = true;

		try {
			reader.close();
		} catch (IOException e) {
			Done = false;
			messageField.append("ERROR: Fail to close BufferedReader!\n");
		}
		writer.close();
		try {
			socket.close();
		} catch (IOException e) {
			Done = false;
			messageField.append("ERROR: Fail to close PrintWriter!\n");
		}

		return Done;

	}
}
