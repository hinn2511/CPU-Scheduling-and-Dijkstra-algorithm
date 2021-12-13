package ltm.client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
					//Hien giao dien
					MainInterface frame = new MainInterface();
					frame.setVisible(true);
					
					client.openSocket();
					
					// Client cho` de nhan public key tu server
					while(true) {
						String publicString = client.receiveString();
						//Nhan duoc public key
						if(publicString != null) {
							System.out.println("Public key in client: " + publicString);
							//Sau khi nhan duoc public key tu server
							//Client gui session key cho server
							client.sendString("this is client session key");
							break;
						}
					};
					
					// Client cho` de nhan phan hoi tu server
					while(true) {
						String ok = client.receiveString();
						if(ok.equals("ok")) {
							break;
						}
					};
					
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		client.closeSocket();
		
		
	}

}
