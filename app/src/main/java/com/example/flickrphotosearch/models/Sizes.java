package com.example.flickrphotosearch.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes {

    @SerializedName("canblog")
    @Expose
    private int canBlog;
    @SerializedName("canprint")
    @Expose
    private int canPrint;
    @SerializedName("candownload")
    @Expose
    private int canDownload;
    @SerializedName("size")
    @Expose
    private List<Size> size = null;

    public int getCanBlog() {
        return canBlog;
    }

    public void setCanBlog(int canBlog) {
        this.canBlog = canBlog;
    }

    public int getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(int canPrint) {
        this.canPrint = canPrint;
    }

    public int getCanDownload() {
        return canDownload;
    }

    public void setCanDownload(int canDownload) {
        this.canDownload = canDownload;
    }

    public List<Size> getSize() {
        return size;
    }

    public void setSize(List<Size> size) {
        this.size = size;
    }

}
