package com.elanelango.poparazzi;

import java.util.ArrayList;

/**
 * Created by eelango on 2/3/16.
 */
public class Photo {
    public String type;
    public String profilePicURL;
    public String username;
    public int createdTime;
    public String caption;
    public String imageUrl;
    public String videoUrl;
    public int videoWidth;
    public int videoHeight;
    public int imageHeight;
    public int imageWidth;
    public int likesCount;

    public ArrayList<Comment> comments = new ArrayList<>();

    public String getTime() {
        long currTime = System.currentTimeMillis() / 1000L;
        int diffTime = (int) (currTime - createdTime);
        if (diffTime < 60)
            return diffTime + "s";
        else if (diffTime < 3600)
            return (diffTime / 60)  + "m";
        else if (diffTime < 86400)
            return (diffTime / 3600) + "h";
        else
            return (diffTime / 86400) + "d";
    }
}
