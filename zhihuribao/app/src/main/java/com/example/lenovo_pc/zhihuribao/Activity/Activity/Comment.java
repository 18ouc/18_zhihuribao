package com.example.lenovo_pc.zhihuribao.Activity.Activity;

import android.graphics.Bitmap;
import android.webkit.WebViewClient;

import java.net.URL;

class Comment {
    private String author;
    private String imageId;
    private String time;
    private String content;
    private String good;


    public Comment() {
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String  getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getimageId() {
        return imageId;
    }

    public void setimageId(String imageId) {
        this.imageId = imageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

}
