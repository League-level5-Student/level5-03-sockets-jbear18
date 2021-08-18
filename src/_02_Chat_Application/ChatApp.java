package _02_Chat_Application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import _00_Click_Chat.networking.Server;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

public class ChatApp extends JFrame implements KeyListener{
	JTextArea textField = new JTextArea();
	JPanel panel = new JPanel();
	JTextPane area = new JTextPane();
	JScrollPane scroll = new JScrollPane(area, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	ChatServer server;
	ChatClient client;
	String password;
	String passwordEnter;
	
	Boolean previousMessageIncoming = null;
	static final int port = 8081;
	Boolean isServer = true;
	
	public static void main(String[] args) {
		new ChatApp();
	}
	
	public ChatApp() {
		int response = JOptionPane.showConfirmDialog(null, "Would you like to host a connection?", "Chat App", JOptionPane.YES_NO_OPTION);
		setPreferredSize(new Dimension(500,350));
		panel.add(scroll);
		panel.add(textField);
		textField.setPreferredSize(new Dimension(450,40));
		scroll.setPreferredSize(new Dimension(450,250));
		textField.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		panel.setBackground(Color.BLACK);
		area.setBackground(Color.GRAY);
		textField.setBackground(Color.GRAY);
		setVisible(true);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(panel);
		
		if(response == JOptionPane.YES_OPTION) {
			isServer = true;
			
			try {
				server = new ChatServer(port, this);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			setTitle("Server");
			password = JOptionPane.showInputDialog("Please create a server passcode");
			JOptionPane.showMessageDialog(null, "Server started at: " + Server.getIPAddress() + "\nPort: " + server.getPort());
			textField.addKeyListener(this);
			System.out.println(password);
			
			server.start();
		}else {
			isServer = false;
			setTitle("CLIENT");
			String ipStr = server.getIPAddress();
			setVisible(false);
			
			System.out.println(ipStr);
			
			passwordEnter = JOptionPane.showInputDialog("Please enter server password");
			client = new ChatClient(ipStr, port, passwordEnter, this);
			textField.addKeyListener(this);
			client.start();
			System.out.println(passwordEnter);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(isServer == false) {
				client.sendClick();
			}else if(isServer) {
				server.sendClick();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void addMessageToWindow(boolean isIncomingMessage, String message) {
		message = message + "\n";
		
		if(message.charAt(0) == '\n') {
			message = message.substring(1, message.length());
		}
		
		System.out.println(Arrays.toString(message.toCharArray()));
		
		if(previousMessageIncoming != null && previousMessageIncoming != isIncomingMessage) {
			appendToPane(area, "\n", Color.GRAY);
		}
		
		previousMessageIncoming = isIncomingMessage;
		
		if(isIncomingMessage) {
			int hex = 0xFF0000;
			int r = (hex & 0xFF0000) >> 16;
			int g = (hex & 0xFF0000) >> 8;
			int b = (hex & 0xFF0000);
			int alpha = 127;
			appendToPane(area, message, new Color(r, g, b, alpha));
		} else {
			int hex = 0xFF0000;
			int r = (hex & 0xFF0000) >> 16;
			int g = (hex & 0xFF0000) >> 8;
			int b = (hex & 0xFF0000);
			int alpha = 127;
			appendToPane(area, "        ", Color.GRAY);
			appendToPane(area, message, new Color(r, g, b, alpha));
		}
	}
	
	private void appendToPane(JTextPane tp, String message, Color bgColor) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
		
		 aset = sc.addAttribute( aset, StyleConstants.FontFamily, "Arial" );
	     aset = sc.addAttribute( aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED );
	     aset = sc.addAttribute( aset, StyleConstants.Background, bgColor );
	     aset = sc.addAttribute( aset, StyleConstants.FontSize, 18 );
	     
	     int len = tp.getDocument().getLength();
	     tp.setCaretPosition( len );
	     tp.setCharacterAttributes( aset, false );
	     tp.replaceSelection( message );
	}
	
//	public ChatServer(int stream) {
//		this.port = port;
//	}
}
