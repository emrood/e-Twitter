package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.FragmentTweet;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity{

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    public TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    FragmentTweet tweety;
    FragmentManager fm;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //create the arraylist
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        fm = getSupportFragmentManager();
        tweety = new FragmentTweet();
        //construire l'adapter
        client = TwitterApplication.getRestClient();//un seul invite
        populateTimeline();
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreTimeline();
                return false;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                populateTimeline();
            }
        });

    }

    //encoyer une requete pour recevoir le timeline
    //
    private void loadMoreTimeline(){
        client.getHomeTimeline(2 ,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(TimelineActivity.this, "Loading more...", Toast.LENGTH_SHORT).show();
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(TimelineActivity.this, "Error loading...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                //JSON comming here
                //creer les models
                //populate into listView
                //ArrayList<Tweet> tweets = Tweet.fromJSOMArray(json);
                aTweets.addAll(Tweet.fromJSONArray(json));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        if (id == R.id.setting_timeline){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNewTweet(View view) {
        tweety.show(fm, "New Tweet");

    }

    public void onResultFromFragment(){

    }


}
