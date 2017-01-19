import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.rmi.RemoteException;

/*
 * Create the GUI for the a NotificationSource
 */
public class NotificationSourceGUI extends JFrame {
	private JPanel contentPane;
	private JTextField sourceName;
	private JButton registerButton, sendButton;
	private JTextArea notification;

	//Constructor which initialises the registerSourceScreen
	public NotificationSourceGUI() throws RemoteException {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 159);
		setVisible(true);
		
		registerSourceScreen();
	}
	
	//Creates the GUI for registering a new source
	public void registerSourceScreen() {		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Create new area for broacasting notifications ");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 36));
		lblNewLabel.setBounds(10, 11, 874, 38);
		contentPane.add(lblNewLabel);
		
		registerButton = new JButton("Register");
		registerButton.setBounds(711, 60, 173, 32);
		contentPane.add(registerButton);
		
		sourceName = new JTextField();
		sourceName.setBounds(10, 60, 680, 32);
		contentPane.add(sourceName);
		sourceName.setColumns(10);
	}
	
	//Creates GUI for the sending notifications to the sinks
	public void sendNotificationScreen(String area) {		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 60, 741, 27);
		contentPane.add(scrollPane);
		
		notification = new JTextArea();
		notification.setLineWrap(true);
		scrollPane.setViewportView(notification);
		
		sendButton = new JButton("Send");
		sendButton.setBounds(772, 60, 112, 27);
		contentPane.add(sendButton);
		
		JLabel lblNewLabel = new JLabel("Broadcasting notifications from " + area);
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 36));
		lblNewLabel.setBounds(10, 11, 874, 38);
		contentPane.add(lblNewLabel);
		
		this.revalidate();
	}

	//Returns the source name
	public JTextField getSourceName() {
		return sourceName;
	}

	//Returns the register button
	public JButton getRegisterButton() {
		return registerButton;
	}

	//Returns the send button
	public JButton getSendButton() {
		return sendButton;
	}

	//Returns the notification typed into the JTextArea
	public JTextArea getNotification() {
		return notification;
	}
}
