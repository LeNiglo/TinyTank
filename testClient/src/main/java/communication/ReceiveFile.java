package communication;

import java.io.*;
import java.net.Socket;

/**
 * Created by Styve on 17/03/2015.
 */

public class ReceiveFile {
    public final static int SOCKET_PORT = 13267;
    private String ip;
    private String path = System.getProperty("user.dir") + "/";

    public ReceiveFile (String ip, String FILE_TO_RECEIVE, int FILE_SIZE) throws IOException {
        this.ip = ip;
        int bytesRead;
        int current;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(this.ip, SOCKET_PORT);
            System.out.println("Downloading " + FILE_TO_RECEIVE + "...");

            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = sock.getInputStream();
            try {
                File theDir = new File(path + "maps");
                theDir.mkdir();
            } catch(SecurityException se){
                //handle it
            }
            fos = new FileOutputStream(path + "maps/" + FILE_TO_RECEIVE);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                if (bytesRead >= 0)
                    current += bytesRead;
            } while (bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("File " + FILE_TO_RECEIVE + " downloaded (" + current + " bytes read)");
        }
        finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
        }
    }

}