import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Defines the basic structure for any Notification sent by the source to the sink
 */
public class Notification implements Serializable {
	private String alert, source;
	private Date date;
	
	/*
	 * Constructor
	 * @param source Name of the source
	 * @param notification Notification message to be sent
	 */
	public Notification(String source, String alert) {
		date = new Date();
		this.alert = alert;
		this.source = source;
	}

	//Returns alert
	public String getAlert() {
		return alert;
	}
	
	//Returns source name
	public String getSource() {
		return source;
	}
	
	//Returns Data and Time when the notification was sent
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return dateFormat.format(date);
	}
}
