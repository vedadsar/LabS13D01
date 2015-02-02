package ba.bitcamp.vedadz.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ba.bitcamp.vedadz.chat.GUI.GUIChat;

public class Server {

	private static final int port = 1717;
	
	public static void serverStart() throws IOException{
		ServerSocket serverSocket = new ServerSocket(port);
	
		while(true){
			String str = "Waiting for connection...";
			System.out.println(str);
			Socket client = serverSocket.accept();			
			GUIChat gc = new GUIChat(client);
			Thread th = new Thread(gc);
			th.start();			
		}
	}
	
	public static void main(String[] args) {
		try {
			serverStart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
