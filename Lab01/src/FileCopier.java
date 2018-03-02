import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopier {
	
	public FileCopier() {	
	}
	
	public void copy(String scr, String dst){
		try {
			byte[] buffer = new byte[1024];
			int len;
			long counter=0;
			boolean append = false;
			File srcFile = new File(scr);
			long filesize = srcFile.length();
			FileInputStream in = new FileInputStream(srcFile);
			
			try {
				File dstFile = new File(dst);
				FileOutputStream out = new FileOutputStream(dstFile);
				
				try {
					while(counter<filesize) {
						len=in.read(buffer,0,buffer.length);
						counter += len;
						//String str = new String(buffer,0,len);
						//System.out.print(str);
						out.write(buffer,0,len);
						append = true;
						out.flush();					
					}
					in.close();
					out.close();
					}catch(IOException ie) {
						System.err.print("IO Exception!");
					}
			}catch(FileNotFoundException ex) {
				System.err.println(String.format("File %s not found!", dst));			
			}
			
		}catch(FileNotFoundException ex) {
			System.err.println(String.format("File %s not found!", scr));		
		}
		
		finally {
			System.out.println("Done!");
		}
	}
}
