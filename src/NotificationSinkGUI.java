import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

//JFrame for SinkGUI
public class NotificationSinkGUI extends JFrame {
	private JPanel contentPane, sourcePanel;
	private JButton registerSink;
	private JTextArea textArea;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField username;
	
	//Constructor
	public NotificationSinkGUI() throws AccessException, RemoteException {
		setResizable(false);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 530);
		setVisible(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);		
		
		registerUser();
	}
	
	//Displays screen to register a user
	public void registerUser() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		username = new JTextField();
		username.setBounds(10, 200, 751, 30);
		getContentPane().add(username);
		username.setColumns(10);
		
		registerSink = new JButton("Register");
		registerSink.setBounds(771, 200, 113, 30);
		getContentPane().add(registerSink);
		
		JLabel lblRegisterToRecieve = new JLabel("Register to recieve notifications");
		lblRegisterToRecieve.setFont(new Font("Calibri", Font.BOLD, 36));
		lblRegisterToRecieve.setBounds(10, 156, 751, 42);
		getContentPane().add(lblRegisterToRecieve);
	}
	
	//Displays main screen where notifications are recieved
	public void notificationsScreen() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setBounds(10, 58, 192, 220);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(scrollPane);
		
		sourcePanel = new JPanel();
		sourcePanel.setBorder(new TitledBorder(null, "Areas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setViewportView(sourcePanel);
		sourcePanel.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("Traffic Updates");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(212, 11, 662, 36);
		contentPane.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 36));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(212, 58, 662, 402);
		scrollPane_1.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(scrollPane_1);
		
		JPanel panel_1 = new JPanel();
		scrollPane_1.setViewportView(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Updates", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setLayout(new BorderLayout());
		String[] header = new String[] {"Time" ,"Source", "Notification"};
		table = new JTable();
		table.setBackground(SystemColor.control);
		tableModel = new DefaultTableModel(0, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		tableModel.setColumnIdentifiers(header);
		table.setModel(tableModel);
		panel_1.add(table.getTableHeader(), BorderLayout.NORTH);
		panel_1.add(table, BorderLayout.CENTER);
		
		JLabel lblArea = new JLabel("Area");
		lblArea.setHorizontalAlignment(SwingConstants.LEFT);
		lblArea.setFont(new Font("Calibri", Font.BOLD, 36));
		lblArea.setBounds(10, 11, 192, 36);
		contentPane.add(lblArea);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 289, 192, 171);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 31, 172, 119);
		scrollPane_2.setBorder(BorderFactory.createEmptyBorder());
		panel_2.add(scrollPane_2);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane_2.setViewportView(textArea);
		textArea.setBackground(SystemColor.control);
		
		this.revalidate();
		this.repaint();
	}
	
	//Clears source panel which contains all the JCheckBoxes with the sources
	public void clearSourcePanel() {
		sourcePanel.removeAll();
	}
	
	//Add JCheckBox to JPanel
	public void addToSourcePanel(JCheckBox checkBox) {
		sourcePanel.add(checkBox);
	}

	//Returns JTextArea of the log
	public JTextArea getTextArea() {
		return textArea;
	}

	//Returns the registerSink JButton
	public JButton getRegisterSink() {
		return registerSink;
	}

	//Returns username/sink nameJTextfield
	public JTextField getUsername() {
		return username;
	}

	//Adds an object[] to the table
	public void addRowToTableModel(Object[] rowData) {
		tableModel.addRow(rowData);
	}
}
