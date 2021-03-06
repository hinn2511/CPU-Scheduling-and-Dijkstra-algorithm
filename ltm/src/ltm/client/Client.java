package ltm.client;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;

import ltm.client.gui.*;

public class Client {
	static Connection client = new Connection();
	
	public static Connection getConnection() {
		return client;
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client.openSocket();
					MainInterface frame = new MainInterface();
					frame.setVisible(true);
					
				} catch (Exception e) {
					System.out.println(e);
				}
				
			}
		});
	}

}
