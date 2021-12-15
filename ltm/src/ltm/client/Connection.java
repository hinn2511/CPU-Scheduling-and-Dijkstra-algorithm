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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Connection {
	private Socket socket = null;
	InputStream inputStream;
	BufferedWriter out = null;
	BufferedReader in = null;
	Security security;

	public void openSocket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		socket = new Socket("127.0.0.1", 1234);
		System.out.println("Client connected");
		inputStream = socket.getInputStream();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		security = new Security();
		security.generateAESKey();

		while(true) {
			String publicString = in.readLine();
			if(publicString != null) {
				security.setPublicKey(publicString);
				String encryptedSessionKey = security.RSAEncrypt(security.getSecretKey());
				out.write(encryptedSessionKey);
				out.newLine();
				out.flush();
				break;
			}
		};
		
		while(true) {
			String se = receiveString();
			if(se.equals("Security enabled")) {
				break;
			}
		};
		
	}

	public void sendString(String message) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		out.write(security.AESEncrypt(message));
		out.newLine();
		out.flush();
	}

	public String receiveString() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String returnMessage = in.readLine();
		String decryptedMessage = security.AESDecrypt(returnMessage);
		return decryptedMessage;
	}

	public void closeSocket() throws IOException {
		if (socket != null) {
			socket.close();
			System.out.print("Connection socket closed");
		}
	}

}
