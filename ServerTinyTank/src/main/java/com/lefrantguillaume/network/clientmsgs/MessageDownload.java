package com.lefrantguillaume.network.clientmsgs;

/**
 * Created by Styve on 25/03/2015.
 */

public class MessageDownload extends MessageModel {
    private String fileName;
    private long fileSize;

    public MessageDownload() {}
    public MessageDownload(String fileName, long fileSize) { this.fileName = fileName; this.fileSize = fileSize; }

    public String getFileName() {return fileName;}
    public long getFileSize() {return fileSize;}
    public void setFileName(String fileName) {this.fileName = fileName;}
    public void setFileSize(long fileSize) {this.fileSize = fileSize;}
}