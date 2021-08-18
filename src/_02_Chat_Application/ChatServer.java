package _02_Chat_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ChatServer extends Thread{
	ServerSocket ss;
	private static int port;
	private static ChatApp app;
	Socket socket;
	
	DataInputStream is;
	DataOutputStream os;
	Boolean correctPassword = false;
	
	public ChatServer(int port, ChatApp app) throws IOException{
		ChatServer.port = port;
		this.app = app;
	}
	
	public void start() {
		System.out.println("Creating ServerSocket...");
		try {
			System.out.println(port);
			ss = new ServerSocket(port, 100);
			
			System.out.println("Server waiting for a client to connect...");
			socket = ss.accept();
			
			System.out.println("The client has connected");
			is = new DataInputStream(socket.getInputStream());
			String message = is.readUTF();
			System.out.println(message);
			os = new DataOutputStream(socket.getOutputStream());
			os.writeUTF("Servers connected!");
			
			
		} catch(SocketTimeoutException s) {
			System.out.println("ERROR SocketTimeoutException");
			s.printStackTrace();
		}catch(IOException e) {
			System.out.println("ERROR IOException");
			e.printStackTrace();
		}
		
		while(socket.isConnected()) {
			try {
				String clientData = is.readUTF();
				if(clientData.startsWith(app.password)) {
					correctPassword = true;
					
					clientData = clientData.substring(app.password.length());
					app.addMessageToWindow(true, clientData);
				}else {
					correctPassword = false;
					os.flush();
					os.writeUTF("Incorrect password, message was unable to be delivered");
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Connection Lost");
				System.exit(0);
			}
		}
	}
	
	public static String getIPAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch(UnknownHostException e) {
			return "ERROR";
		}
	}
	
	public static int getPort() {
		return port;
	}
	
	public void sendClick() {
		try {
			if(os != null) {
				if(correctPassword) {
					os.writeUTF(app.textField.getText());
					System.out.println(app.area);
					app.addMessageToWindow(false, app.textField.getText());
					os.flush();
					app.textField.setText("");
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		}
	}
	
