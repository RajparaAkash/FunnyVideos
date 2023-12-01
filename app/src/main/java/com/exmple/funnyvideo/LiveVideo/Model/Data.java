package com.exmple.funnyvideo.LiveVideo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("Title")
    @Expose
    private String Title;
    @SerializedName("Category")
    @Expose
    private  String Category;
    @SerializedName("views")
    @Expose
    private int views;
    @SerializedName("likes")
    @Expose
    private int likes;

    public Data(String ID, String title, String category, int views, int likes) {
        this.ID = ID;
        this.Title = title;
        this.Category = category;
        this.views = views;
        this.likes = likes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Title = category;
    }


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


}
