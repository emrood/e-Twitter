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

    //liste des attributs
    private String imageLast;
    private int nbrRetweet;
    private User user;
    private String createAt;
    private String body;
    private  long uid;
    private String tweetImage1;
    private String tweetImage2;
    private int followers;
    private int following;
    private String userDescription;
    private String screenName;


    public void setImageLast(String imageLast) {
        this.imageLast = imageLast;
    }

    public void setNbrRetweet(int nbrRetweet) {
        this.nbrRetweet = nbrRetweet;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }



    public String getScreenName() {
        return screenName;
    }


    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public String getUserDescription() {
        return userDescription;
    }


    public String getTweetImage1() {
        return tweetImage1;
    }

    public String getTweetImage2() {
        return tweetImage2;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreateAt() {
        return createAt;
    }
    public User getUser() {
        return user;
    }



    public String getImageLast() {
        return imageLast;
    }




    public int getNbrRetweet() {
        return nbrRetweet;
    }




    //deserialize the json
    //Tweet.fromJSON({})
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        //prendre la valeur du json

        try {
            tweet.uid = jsonObject.getLong("id");
            tweet.nbrRetweet = jsonObject.getInt("retweet_count");
            tweet.body = jsonObject.getString("text");
            tweet.screenName = jsonObject.getJSONObject("user").getString("screen_name");
            tweet.createAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.imageLast = jsonObject.getJSONObject("user").getString("profile_image_url");
            tweet.followers = jsonObject.getJSONObject("user").getInt("followers_count");
            tweet.following = jsonObject.getJSONObject("user").getInt("friends_count");
            tweet.userDescription = jsonObject.getJSONObject("user").getString("description");
            tweet.tweetImage1 = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
            tweet.tweetImage2 = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).getString("media_url");
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
