package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Emmanuel Roodly on 03/08/2017.
 */
public class User {
    //attributs
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String profileImagehttps;
    private String profileImage3;
    private String tagLine;
    private int follower;
    private int following;


    //Getter
    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getUid() {
        return uid;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public String getTagLine() {
        return tagLine;
    }

    public int getFollower() {
        return follower;
    }

    public int getFollowing() {
        return following;
    }
    public String getProfileImagehttps() {
        return profileImagehttps;
    }

    public String getProfileImage3() {
        return profileImage3;
    }



    //prendre les donnees du json
    public static User fromJSON(JSONObject json){

        User u = new User();

        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.profileImagehttps = json.getString("profile_image_url_https");
            u.profileImage3 = json.getString("profile_background_image_url_https");
            u.tagLine = json.getString("description");
            u.follower = json.getInt("followers_count");
            u.following = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //retourne un utilisateur
        return u;
    }
}
