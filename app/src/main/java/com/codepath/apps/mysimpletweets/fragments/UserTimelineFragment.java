package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.swipeContainer;
import static java.util.Collections.addAll;

/**
 * Created by Emmanuel Roodly on 09/08/2017.
 */

public class UserTimelineFragment extends TweetsListFragment{

    private TwitterClient client;
    //public ArrayList<Tweet> tweets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();//un seul invite
        //tweets = new ArrayList<>();
        populateTimeline();


    }

    public static UserTimelineFragment newInstance(String screen_name){
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimeline() {
        String screen_name = getArguments().getString("screenName");
        if(isNetworkAvailable()){
            if(isOnline()){
                client.getUserTimeline(screen_name, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        Log.d("DEBUG", json.toString());
                        //JSON comming here
                        //creer les models
                        //populate into listView
                        //ArrayList<Tweet> tweets = Tweet.fromJSOMArray(json);
                        addAll(Tweet.fromJSONArray(json));
                        swipeContainer.setRefreshing(false);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //Log.d("DEBUG", errorResponse.toString());
                    }
                });
            } else {
                Toast.makeText(getContext(), "Poor connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Use Wi-fi or Mobile data", Toast.LENGTH_SHORT).show();
        }
    }
}
