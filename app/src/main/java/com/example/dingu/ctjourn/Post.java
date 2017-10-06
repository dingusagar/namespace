package com.example.dingu.ctjourn;



public class Post {
    private String Title;
    private String video;
    private String Desc;



    private String username;

    public Post()
    {

    }
    public Post(String title, String desc, String video,String userName) {
        Title = title;
        Desc = desc;
        video = video;
        this.username = userName;
    }
    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }
    public String getVideo() {
        return video;
    }

    public void setVideo(String image) {
        video = video;
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
