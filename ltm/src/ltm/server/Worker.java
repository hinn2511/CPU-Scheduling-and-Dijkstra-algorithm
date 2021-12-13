package ltm.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import ltm.server.pathFinder.CalculatePath;
import ltm.server.pathFinder.CalculatePath2;

public class Worker implements Runnable {
	String publicKeyString = "this is server public key";
	String privateKeyString = "this is server private key";
	String privateSessionString = "";
	BufferedReader in = null;
	BufferedWriter out = null;

	private Socket socket;

	public Worker(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void sendString(String message) throws IOException {
		out.write(message);
		out.newLine();
		out.flush();
	}

	public String receiveString() throws IOException {
		String returnMessageString = in.readLine();
		return returnMessageString;
	}

	@Override
	public void run() {
		System.out.println("Connection " + socket.toString() + " accepted");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			// Server gui public key cho client dau tien
			sendString(publicKeyString);

			// Cho client gui sesson key
			while (true) {
				String sessionKey = receiveString();

				// Nhan duoc session key tu client
				if (sessionKey != null) {
					System.out.println("Session Key " + sessionKey);
					//Sau khi nhan duoc session key tu server
					//Server gui phan hoi cho client
					privateSessionString = sessionKey;
					sendString("ok");
					break;
				}
			}
			
			while (true) {
				String request = in.readLine();
				if (request != null) {
					if (request.equals("exit"))
						break;
					if (request.equals("findPath")) {
						String data = in.readLine();
						
						StringTokenizer st = new StringTokenizer(data, "|");
						String placeList = st.nextToken().replace("Places:", "");
						boolean directed = Boolean.valueOf(st.nextToken().replace("Directed:", "")) ;
						String from = st.nextToken().replace("From:", "");
						String to = st.nextToken().replace("To:", "");
						String distance = st.nextToken();
	
						CalculatePath2 calculatePath2 = new CalculatePath2(placeList, directed, from, to, distance);
						if (calculatePath2.getCost() != Double.MAX_VALUE) {
							sendString("success");
							
							sendString(calculatePath2.getPlacelist());
							sendString(calculatePath2.getStartPlace());
							sendString(calculatePath2.getEndPlace());
							sendString(calculatePath2.getShortestPath());
							sendString(String.valueOf((int) calculatePath2.getCost()));
							sendString(calculatePath2.getPathGraph());
							sendString(calculatePath2.getShortestPathGraph());
						} else {
							sendString("failed");
							sendString("Can not find shortest path");
						}
					}
					if (request.equals("cpuScheduling")) {

					}
					//out.flush();
				}

			}
			System.out.println("Closing connection");
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Exception " + e);
		} 
	}

}
