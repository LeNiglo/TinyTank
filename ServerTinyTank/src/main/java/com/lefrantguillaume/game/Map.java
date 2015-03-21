package com.lefrantguillaume.game;

/**
 * Created by Styve on 19/03/2015.
 */
public class Map {
    private String name;
    private String fileName;
    private String filePath;
    private long fileLength;
    private String imgName;
    private String imgPath;
    private long imgLength;
    private String fileNameNoExt;

    public Map() {}

    public String getName() {
        return name;
    }
    public String getFileName() {return fileName;}
    public String getFilePath() {return filePath;}
    public long getFileLength() {return fileLength;}
    public String getImgName() {return imgName;}
    public String getImgPath() {return imgPath;}
    public long getImgLength() {return imgLength;}
    public String getFileNameNoExt() {return fileNameNoExt;}
    public void setName(String name) {
        this.name = name;
    }
    public void setFileName(String fileName) {this.fileName = fileName;}
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public void setFileLength(long fileLength) {this.fileLength = fileLength;}
    public void setImgName(String imgName) {this.imgName = imgName;}
    public void setImgPath(String imgPath) {this.imgPath = imgPath;}
    public void setImgLength(long imgLength) {this.imgLength = imgLength;}
    public void setFileNameNoExt(String fileNameNoExt) {this.fileNameNoExt = fileNameNoExt;}
}
