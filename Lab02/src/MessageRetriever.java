
import java.io.*;
import java.util.Scanner;



public class MessageRetriever {
	public static void retrieve(String fileName) throws FileNotFoundException, IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(fileName));
		while(in.available() > 0){
			int length = in.readInt();
			byte[] ch = new byte[1024];
			in.read(ch, 0, length);
			for(int i = 0; i < length; i++){
				System.out.print((char)ch[i]);
			}
			System.out.println();
		}
		in.close();
	}

	
	public static void main(String args[]) throws FileNotFoundException, IOException{
		
		Scanner kb = new Scanner(System.in);
		System.out.print("Input your destination file name: ");
		String fileName = kb.nextLine();
		MessageLogger msg = new MessageLogger(fileName);
		msg.act();
		MessageRetriever.retrieve(fileName);
		kb.close();
		
	}

}
