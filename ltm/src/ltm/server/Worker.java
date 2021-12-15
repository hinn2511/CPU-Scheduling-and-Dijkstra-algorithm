package ltm.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ltm.server.pathFinder.CalculatePath;

public class Worker implements Runnable {
	Security security;
	BufferedReader in = null;
	BufferedWriter out = null;
	private Socket socket;

	public Worker(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void send(String message) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String encryptedMessage = security.AESEncrypt(message);
		out.write(encryptedMessage);
		out.newLine();
		out.flush();
	}

	public String receive() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String returnMessage = in.readLine();
		if (returnMessage == null)
			return null;
		String decryptedMessage = security.AESDecrypt(returnMessage);
		return decryptedMessage;
	}

	@Override
	public void run() {
		System.out.println("Connection " + socket.toString() + " accepted");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			security = new Security();
			security.generateRSAKey();

			out.write(security.getPublicKey());
			out.newLine();
			out.flush();

			while (true) {
				String encryptedSessionKey = in.readLine();
				if (encryptedSessionKey != null) {
					String sessionKey = security.RSADecrypt(encryptedSessionKey);
					security.setSecretKey(sessionKey);
					send("Security enabled");
					break;
				}
			}

			while (true) {
				String request = receive();
				if (request != null) {
					if (request.equals("exit"))
						break;
					if (request.equals("findPath")) {
						String data = receive();

						ValidatePathString validate = new ValidatePathString(data);
						if (!validate.getError().equals("noerror")) {
							send("error");
							send(validate.getError());
						} 
						else {
							CalculatePath calculate = new CalculatePath(validate.getPlaceList(),
									validate.getFrom(), validate.getTo(), validate.getDistance());
							if (calculate.getCost() != Double.MAX_VALUE) {
								send("success");
								send(calculate.getPlacelist());
								send(calculate.getStartPlace());
								send(calculate.getEndPlace());
								send(calculate.getShortestPath());
								send(String.valueOf((int) calculate.getCost()));
								send(calculate.getPathGraph());
								send(calculate.getShortestPathGraph());
							} else {
								send("failed");
								send("Can not find shortest path");
							}
						}

					}
					if (request.equals("cpuScheduling")) {

					}
				}

			}
			System.out.println("Closing connection");
			in.close();
			out.close();
			socket.close();
		} catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException
				| NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Exception " + e);
		}
	}

}
