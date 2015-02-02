package ba.bitcamp.vedadz.chat.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Simple method 
 * @author vedadzornic
 *
 */
public class Client {
	
	private static final int port = 1717;
	private static final String host = "localhost";
	
	
	public static void main(String[] args) {
		try {
			Socket client = new Socket(host, port);
			OutputStream os = client.getOutputStream();
			os.write("HELLO WORLD \n".getBytes());
			os.flush();
			while(true){
				
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
