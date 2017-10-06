package com.example.dingu.ctjourn;


import java.util.HashMap;

public class Post {

    public Post(){}

    public int getPostID() {
        return postID;
    }

    public Post(String title, String desc, String video, String username) {
        Title = title;
        this.video = video;
        Desc = desc;
        this.username = username;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    private int postID;
    private String Title;
    private String video;
    private String Desc;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    private HashMap<String, Integer> votes;
    private String username;

    public HashMap<String, Integer> getVotes() {
        return votes;
    }

    public void setVotes(HashMap<String, Integer> votes) {
        this.votes = votes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}