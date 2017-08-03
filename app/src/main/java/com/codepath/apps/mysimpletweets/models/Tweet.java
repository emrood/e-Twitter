package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Emmanuel Roodly on 03/08/2017.
 */

//
public class Tweet {
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreateAt() {
        return createAt;
    }

    //liste des attributs
    private String body;
    private  long uid;

    public User getUser() {
        return user;
    }

    private User user;
    private String createAt;


    //deserialize the json
    //Tweet.fromJSON({})
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        //prendre la valeur du json

        try {
            tweet.uid = jsonObject.getLong("id");
            tweet.body = jsonObject.getString("text");
            tweet.createAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return  tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i <= jsonArray.length(); i++){
            try{
                JSONObject tweetjson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetjson);
                if(tweet != null){
                    tweets.add(tweet);
                }
            }catch (JSONException e){
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

}
