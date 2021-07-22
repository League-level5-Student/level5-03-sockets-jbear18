package _02_Chat_Application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

public class ChatApp {
	private int port;
	private ServerSocket serverSocket;
	private Socket connection;
	
	ObjectOutputStream os;
	ObjectInputStream is;
	
//	public ChatServer(int stream) {
//		this.port = port;
//	}
}
