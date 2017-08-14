package com.codepath.apps.mysimpletweets.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.DatabaseHelper;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TweetDataBase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Emmanuel Roodly on 08/08/2017.
 */

//Fragment HomeTimeline

public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient client;
    public TweetsListFragment tweetsListFragment;
    public SwipeRefreshLayout swipeContainer;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();//un seul invite

        //tweetDataBase = new TweetDataBase(getContext());

        swipeContainer = new SwipeRefreshLayout(getContext());

        populateTimeline();

        //Prise en charge d'un refraichissement du Timeline --> a remplacer dans  fragment mere
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                populateTimeline();
            }
        });
    }

    public void doRefresh() {
        aTweets.clear();
        populateTimeline();
    }

    //methode visant a recevoir la suite du Timeline (when we scroll down)
    private void loadMoreTimeline() {
        client.getHomeTimeline(2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                Toast.makeText(getActivity(), "Loading more...", Toast.LENGTH_SHORT).show();
                addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Error loading...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Pour remplir la listeView
    public void populateTimeline() {
        if (isNetworkAvailable()) {//si source de donnee ouvert | Wifi | Donnee mobile
            if (isOnline()) {//si connection  a internet possible | ping google
                client.getHomeTimeline(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        Log.d("DEBUG", json.toString());

                        addAll(Tweet.fromJSONArray(json));
                        //tweetDataBase.addAllTweet(Tweet.fromJSONArray(json));
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //Log.d("DEBUG", errorResponse.toString());
                    }
                });
            } else {
                Toast.makeText(getContext(), "Poor connection", Toast.LENGTH_SHORT).show();
                //populateFromDB();
            }
        } else {
            Toast.makeText(getContext(), "Use Wi-fi or Mobile data", Toast.LENGTH_SHORT).show();
            //populateFromDB();
        }
    }



}
