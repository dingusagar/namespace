package com.example.dingu.ctjourn;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

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
    private Double latitude, longitude;


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

    public void setGPSCoordinates(Context context) {
        LocationManager locationManager;
        LocationListener locationListener;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
    }


}