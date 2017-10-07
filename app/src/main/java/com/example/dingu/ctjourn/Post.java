package com.example.dingu.ctjourn;




import java.util.HashMap;

public class Post {

    public Post() {
    }

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
    private double latitude;
    private float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;



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

    private HashMap<String, Integer> votes = new HashMap<>();
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