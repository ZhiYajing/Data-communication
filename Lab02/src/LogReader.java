import java.io.*;
import java.util.Scanner;

public class LogReader {
	DataInputStream in =null;

	public LogReader(String fileName) throws FileNotFoundException {
		in = new DataInputStream(new FileInputStream(fileName));
	}
	
	public void act() throws IOException{
		int length;
		while (in.available() > 0) {
			length = in.readInt();
			byte[] buffer = new byte[1024];
			in.read(buffer,0,length);
			for(int i = 0; i <length;i++) {
				System.out.print((char)buffer[i]);
			}
			System.out.println();
		}
		in.close();
	}

public static void main (String[] args) throws IOException{
//	MessageLogger logger = new MessageLogger("message.log");
//	logger.act();
	LogReader reader = new LogReader("message.log");
	reader.act();
}
}

