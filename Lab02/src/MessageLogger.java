import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class MessageLogger {
	
	DataOutputStream out = null;
	
	public MessageLogger(String fileName) throws FileNotFoundException {
		out = new DataOutputStream(new FileOutputStream(fileName));
	}
	
	public void act() throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please input messages. Type \"/END\" if you want to stop");
		String msg;
		while (!(msg = scanner.nextLine()).equalsIgnoreCase("/END")) {
			out.writeInt(msg.length());
			out.writeBytes(msg);
		}
		
		out.flush();
		out.close();
		scanner.close();
	}
	
	public static void main(String[] args) throws IOException {
		MessageLogger logger = new MessageLogger("message.log");
		logger.act();
	}

}
