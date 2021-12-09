package ltm.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection {
	private Socket socket = null;
	InputStream inputStream;
	BufferedWriter out = null;
	BufferedReader in = null;

	public void openSocket() throws IOException {
		socket = new Socket("127.0.0.1", 1234);
		System.out.println("Client connected");
		inputStream = socket.getInputStream();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

	}

	public void sendString(String string) throws IOException {
		out.write(string);
		out.newLine();
		out.flush();
	}

	public String receiveString() throws IOException {
		String returnMessageString = in.readLine();
		return returnMessageString;
	}

	public void closeSocket() throws IOException {
		if (socket != null) {
			socket.close();
			System.out.print("Connection socket closed");
		}
	}

}
