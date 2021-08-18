package _02_Chat_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ChatClient {

	private int port;
	private String ip;
	private String passwordEnter;
	private ChatApp app;
	
//	private ServerSocket serverSocket;
//	private Socket connection;
	
DataOutputStream os;
DataInputStream is;
	
	public ChatClient(String ip, int port, String passwordEnter, ChatApp app) {
		this.ip = ip;
		this.port = port;
		this.passwordEnter = passwordEnter;
		this.app = app;
	}
	
	public void start() {
		try {
			Socket s = new Socket(ip, port);
			os = new DataOutputStream(s.getOutputStream());
			os.writeUTF("message");
			is = new DataInputStream(s.getInputStream());
			os.writeUTF(passwordEnter + "Servers Connected!");
			
			
				app.setVisible(true);
				System.out.println("Passowrd Is Correct");
				
			while(s.isConnected()) {
				try {
					String clientData = is.readUTF();
					app.addMessageToWindow(true, clientData);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			String message = is.readUTF();
			System.out.println(message);
			s.close();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Connect was lost");
			System.out.println("Error occurred");
			e.printStackTrace();
		}
	}
	public void sendClick() {
		try {
			if(os != null) {
				os.writeUTF(passwordEnter + app.textField.getText());
				app.addMessageToWindow(false, app.textField.getText());
				os.flush();
				app.textField.setText("");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
