package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Emmanuel Roodly on 13/08/2017.
 */

public class Message {

    ///attributs
    String date;
    String body;
    String screenName;
    String profilepic;

    //Getter
    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfilepic() {
        return profilepic;
    }



    public static Message fromJSON(JSONObject jsonObject){
        Message message = new Message();

        try{
            message.date = jsonObject.getString("created_at");
            message.body = jsonObject.getString("text");
            message.screenName = jsonObject.getString("recipient_screen_name");
            message.profilepic = jsonObject.getJSONObject("recipient").getString("profile_image_url");

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  message;
    }

    public static ArrayList<Message> fromJSONArray(JSONArray jsonArray){
        ArrayList<Message> messages = new ArrayList<>();
        for(int i = 0; i <= jsonArray.length(); i++){
            try{
                JSONObject messagejson = jsonArray.getJSONObject(i);
                Message message = Message.fromJSON(messagejson);
                if(message != null){
                    messages.add(message);
                }
            }catch (JSONException e){
                e.printStackTrace();
                continue;
            }
        }
        return messages;
    }
}
