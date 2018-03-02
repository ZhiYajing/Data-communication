import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class IntReader {

	public static void act() throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream("test.dat"));
		int n;
		while (in.available() > 0) {
			n = in.readInt();
			System.out.println(n);
		}
	}

	public static void main(String[] args) throws IOException {
		IntReader.act();
	}
}
