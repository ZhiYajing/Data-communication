import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class lab01 {
	public static void main(String[] args) throws IOException{
		String src, dst;
		Scanner scanner =new Scanner(System.in);
		
		System.out.println("Please input the source file name");
		src=scanner.nextLine();
		
		System.out.println("Please input the destination file name");
		dst=scanner.nextLine();
		
		FileCopier copier = new FileCopier();
		copier.copy(src, dst);
		
		scanner.close();
	
	}
}
