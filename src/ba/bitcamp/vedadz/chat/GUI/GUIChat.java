package ba.bitcamp.vedadz.chat.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author vedadzornic
 *
 */
public class GUIChat  implements Runnable{

	private JTextArea display;
	private JTextField textInput;
	private Socket connection;
	private InputStream is;
	private OutputStream os;
	

	/**
	 * Constructor for creating gui chat frame. Frame has text area which
	 * represents all messages, text field which represents text user want to
	 * type and send and send button for sending messages.
	 */
	public GUIChat(Socket connection) {
		this.connection = connection;
		
		try {
			is = connection.getInputStream();
			os = connection.getOutputStream();
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
		
		
		// Creating frame,panel,text areas and button.
		JFrame chatFrame = new JFrame();
		JButton send = new JButton("SEND");
		display = new JTextArea();
		textInput = new JTextField();
		JPanel chatPanel = new JPanel();
		
		/**
		 * ScrollPane is panel with scroller in it. We'll add display on scroll pane and then add scroll
		 * pane on main panel. Also we're setting scroll pane shows only when its needed and setting prefered dimension
		 * of scroll pane. In those dimension we have to include display too.
		 */
		JScrollPane sb = new JScrollPane(display);
		sb.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sb.setPreferredSize(new Dimension(350, 180));
		
		
		
		// Setting sizes of attributes		
		textInput.setPreferredSize(new Dimension(250, 50));
		send.setPreferredSize(new Dimension(100, 50));
		display.setEditable(false);
		display.setLineWrap(true);						//for wrapping text lines.
		
		ButtonHandler bh = new ButtonHandler();
		send.addActionListener(bh);

		// Setting frame properties
		chatFrame.setSize(400, 280);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setResizable(false);

		// Adding elements on panel and then panel to frame
		chatPanel.add(sb);
		chatPanel.add(textInput);
		chatPanel.add(send);
		chatFrame.setContentPane(chatPanel);
		
		textInput.addKeyListener(new KeyAdapter() {			//Creating key adapter			
			/**
			 * Key addepter method checking if Enter was pressed on textInput.
			 */
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					try {
						sendMessage();
					} catch (IOException e1) {
						System.out.println(e1.getMessage());
					}				
			}
		});

		chatFrame.setVisible(true); // Setting our frame visible
	}

	public void listenForNetwork() throws IOException{
		BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(is));	//Buffered reader from InputStreamReader.
		String line = null;
		while((line = bufferedInput.readLine()) != null){		//adding line to line variable and checking while its not null.
				
			if(!line.equals(""))	{					
				display.append("Client: " +line +"\n");				
				line = null;					//setting line to null!
			}
				
		}
	}
	
	
	private class ButtonHandler implements ActionListener {
		@Override
		/**
		 * On action performed getting text from text field, sending it on text area and clearing text field again.
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				sendMessage();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		}

	}
	
	/**
	 * Method which is sending messages. 
	 * It gets message from our message box, send it to display text field.
	 * @throws IOException 
	 */
	private void sendMessage() throws IOException{
		
		textInput.setText(textInput.getText().trim());			//Trim check in case whole message is filled with spaces.		
		String text = textInput.getText();
		
		if (text.equals("")) {
			System.out.println("blank text");
		} else {
			text += "\n";
			display.append("ME: " +text );
			os.write(text.getBytes());
			textInput.setText(null);
		}
	}

	@Override
	public void run() {
		try {
			listenForNetwork();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
