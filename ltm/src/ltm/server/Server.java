package ltm.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	public static int port = 1234;
	public static int numThread = 5;
	private static ServerSocket server = null;
	
	public static void main(String args[]) throws IOException {
		ExecutorService executor = Executors.newFixedThreadPool(numThread);
		try {
			server = new ServerSocket(port);
			System.out.println("Server started!");
			System.out.println("Waiting for a Client...");
			while(true)
			{
				Socket socket = server.accept();
				executor.execute(new Worker(socket));
			}
		} 
		catch (IOException e) {
			// TODO: handle exception
		}
		finally {
			if(server != null)
				server.close();
		}
	}
}