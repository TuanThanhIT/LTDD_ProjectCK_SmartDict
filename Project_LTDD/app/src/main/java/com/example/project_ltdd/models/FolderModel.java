package com.example.project_ltdd.models;

import java.io.Serializable;

public class FolderModel implements Serializable {

    private int folder_id;
    private String folder_name;

    public int getFolderId() {
        return folder_id;
    }

    public void setFolderId(int folderId) {
        this.folder_id = folderId;
    }

    public String getFolderName() {
        return folder_name;
    }

    public void setFolderName(String folderName) {
        this.folder_name = folderName;
    }


    public FolderModel(int folderId, String folderName) {
        this.folder_id = folderId;
        this.folder_name = folderName;
    }

    public FolderModel(String folderName) {
        this.folder_name = folderName;
    }
}

