package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Emmanuel Roodly on 03/08/2017.
 */
public class User {
    //attributs
    private String name;

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



    private long uid;
    private String screenName;
    private String profileImageUrl;

    public String getProfileImagehttps() {
        return profileImagehttps;
    }

    public String getProfileImage3() {
        return profileImage3;
    }

    private String profileImagehttps;
    private String profileImage3;

    //prendre les donnees du json
    public static User fromJSON(JSONObject jsonObject){

        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.profileImagehttps = jsonObject.getString("profile_image_url_https");
            u.profileImage3 = jsonObject.getString("profile_background_image_url_https");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //retourne un utilisateur
        return u;
    }
}
