
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CommandPrompt {
	File currentDir = new File(".");

	public void exec(String statement) throws IOException {
		String command = null;
		String option = null;
		String msg;
		
		int endIdx = statement.trim().indexOf(' ');
		if (endIdx > 0) {
			command = statement.substring(0, endIdx).trim();
			option = statement.substring(endIdx + 1).trim();
		} else
			command = statement;

		switch (command.toLowerCase()) {
		case "cd":
			changeDir(option);
			break;
		case "dir":
			listFiles(option);
			break;
		case "freespace":
			msg = String.format("The free space is %,d bytes.", checkFreespace(option));
			System.out.println(msg);
			break;
		// add more cases here for different commands

			
		default:
			msg = String.format("'%s' is not recognized as an command.", command);
			System.out.println(msg);
			break;
		}
	}

	private long checkFreespace(String path) throws IOException {
		File dir = new File(path);
		return dir.getFreeSpace();
	}

	private void changeDir(String path) throws IOException {	
		//if (path.startsWith("\"")) {
		File testAbsolute = new File(path);
		if (testAbsolute.isAbsolute()) { 
		currentDir = new File(path);
		}else {
		currentDir = new File(currentDir.getCanonicalPath()+"/"+path);
		}
		//System.out.println(currentDir);
		System.out.println(currentDir.getCanonicalPath());
	}

	private String getInfo(File f) {
		Date data = new Date(f.lastModified());
		String ld= new SimpleDateFormat("MMM dd,yyyy").format(data);
		if(f.isFile()) {
			return String.format("%dKB\t%s\t%s",
					(int) Math.ceil((float) f.length() / 1024),
					ld, f.getName());
			} else
		    return String.format("<DIR>\t%s\t%s", ld, f.getName());
	}
	

	private void listFiles(String path) {
		File dir= new File(path);
		File[] fileList= dir.listFiles();
		String info= "";
		for(int i= 0; i< fileList.length; i++)
		info+= getInfo(fileList[i]) + "\n";
		System.out.println(info);
	}

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		String statement;
		CommandPrompt prompt = new CommandPrompt();

		while (true) {
			System.out.print("> ");
			statement = scanner.nextLine().trim();
			if (statement.equalsIgnoreCase("exit"))
				break;

			prompt.exec(statement);
		}
		scanner.close();
	}

}
