package com.lefrantguillaume.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SendFile {
    public final static int SOCKET_PORT = 13267;
    private String path = System.getProperty("user.dir") + "\\";

    public SendFile(String FILE_TO_SEND) throws IOException {
        FileInputStream fis;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            servsock = new ServerSocket(SOCKET_PORT);
            try {
                sock = servsock.accept();
                // send file
                File myFile = new File(path + FILE_TO_SEND);
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                os = sock.getOutputStream();
                System.out.println("Sending " + FILE_TO_SEND + " (" + mybytearray.length + " bytes) to " + sock.getInetAddress().getHostAddress());
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
                System.out.println("Done.");
            } finally {
                if (bis != null) bis.close();
                if (os != null) os.close();
                if (sock != null) sock.close();
            }
        } finally {
            if (servsock != null) servsock.close();
        }
    }
}