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
    public static User fromJSON(JSONObject jsonObject){

        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.profileImagehttps = jsonObject.getString("profile_image_url_https");
            u.profileImage3 = jsonObject.getString("profile_background_image_url_https");
            u.tagLine = jsonObject.getString("description");
            u.follower = jsonObject.getInt("followers_count");
            u.following = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //retourne un utilisateur
        return u;
    }
}
