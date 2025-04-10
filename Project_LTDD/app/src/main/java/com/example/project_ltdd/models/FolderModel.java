package com.example.project_ltdd.models;

import java.io.Serializable;

public class FolderModel implements Serializable {

    private int folderId;
    private String folderName;

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }


    public FolderModel(int folderId, String folderName) {
        this.folderId = folderId;
        this.folderName = folderName;
    }

    public FolderModel(String folderName) {
        this.folderName = folderName;
    }
}

