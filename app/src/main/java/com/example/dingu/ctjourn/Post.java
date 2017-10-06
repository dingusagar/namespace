package com.example.dingu.ctjourn;

/**
 * Created by dingu on 1/4/17.
 */

public class Post {
    private String Title;
    private String Image;
    private String Desc;



    private String username;

    public Post()
    {

    }
    public Post(String title, String desc, String image, String userName) {
        Title = title;
        Desc = desc;
        Image = image;
        this.username = userName;
    }
    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
