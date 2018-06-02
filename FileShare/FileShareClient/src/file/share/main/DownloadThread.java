package file.share.main;

import java.io.*;
import java.net.Socket;

public class DownloadThread implements Runnable{
    private String host;
    private String port;
    private String password;
    private String filePath;
    private String dir;
    private int size;

    public DownloadThread(String host, String port, String password, String filePath,String dir,int size) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.filePath = filePath;
        this.dir = dir;
        this.size = size;

    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host,Integer.parseInt(port));
            PrintStream out = new PrintStream(socket.getOutputStream());
            InputStream in = socket.getInputStream();
            out.println("password:" + password);
            out.println("download:"+filePath);
            socket.shutdownOutput();
            int begin = filePath.lastIndexOf("/")+1;
            String fileName = filePath.substring(begin);
            File temp = new File(dir);
            if(!temp.exists()){
                temp.mkdirs();
            }

            FileOutputStream fileOut = new FileOutputStream(dir+"/"+fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int total = 0;
            int length = in.read(buf);
            total+=length;
            bos.write(buf, 0, length);
            while(total<size)
            {
                length = in.read(buf);
                bos.write(buf, 0, length);
                total+=length;
            }

            bos.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
