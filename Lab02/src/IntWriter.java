import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class IntWriter {

	public static void act() throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream("test.dat"));
		Scanner scanner = new Scanner(System.in);
		int n;
		while(true) {
			try {
				n = scanner.nextInt();
			} catch (InputMismatchException ex) {
				break;
			}
			out.writeInt(n);
		}
		scanner.close();
		out.close();
	}
	
	public static void main(String[] args) throws IOException {
		IntWriter.act();
	}

}
