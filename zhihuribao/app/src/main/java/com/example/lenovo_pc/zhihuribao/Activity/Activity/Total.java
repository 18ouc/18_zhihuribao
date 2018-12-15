package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.graphics.Bitmap;
import android.webkit.WebViewClient;

import java.net.URL;

class Total {
    private String name;
    private String  imageId;
    private String description;
    private  int id;

    public Total() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getimageId() {
        return imageId;
    }

    public void setimageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
