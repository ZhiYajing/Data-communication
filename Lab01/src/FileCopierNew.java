

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by arthursdays on 6/2/2017.
 */
public class FileCopierNew {
    private String sFileName;
    private String tFileName;

    public FileCopierNew(String sFileName, String tFileName){
        this.sFileName = sFileName;
        this.tFileName = tFileName;

    }
    public void copy(){
        FileCopierNew.copy(sFileName, tFileName);
    }

    public static void copy(String sFileName, String tFileName){
        try{
            FileInputStream in = new FileInputStream(sFileName);
            byte[] content = new byte[1024];
            boolean append = false;
            try{
                FileOutputStream out = new FileOutputStream(tFileName, append);
                int length = 1024;
                //int pos = 0;
                try {
                    while ((length = in.read(content)) > 0) {
                        //String str = new String(content, 0, length);
                        out.write(content, 0, length);
                        append = true;
                        out.flush();
                    }
                    in.close();
                    out.close();
                }catch(IOException ie){
                    System.err.println("IO Exception!");
                }
            }catch(FileNotFoundException fe){
                System.err.println(String.format("File %s not found!", tFileName));
            }
        }catch(FileNotFoundException fe){
            System.err.println(String.format("File %s not found!", sFileName));
        }finally{
            System.out.println("Done!");
        }
    }
}
