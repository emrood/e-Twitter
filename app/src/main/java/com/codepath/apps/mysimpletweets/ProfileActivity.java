package com.codepath.apps.mysimpletweets;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.media.CamcorderProfile.get;
import static com.codepath.apps.mysimpletweets.R.id.ivProfileImage;
import static com.codepath.apps.mysimpletweets.R.id.progressBar;

import static com.codepath.apps.mysimpletweets.R.id.swipeContainer;
import static com.codepath.apps.mysimpletweets.TwitterApplication.getRestClient;
import static java.util.Collections.addAll;

public class ProfileActivity extends AppCompatActivity {
    public TwitterClient client;
    public User user;
    public Tweet myTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        user = new User();
        myTweet = new Tweet();
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                user = User.fromJSON(response);// my curretnt user account info
                getSupportActionBar().setTitle("e-Twitter | @" + user.getScreenName());
            }
        });
        client.getUserTimeline( user.getScreenName() ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                //JSON comming here
                //creer les models
                //populate into listView
                ArrayList<Tweet> tweets = new ArrayList<>();
                tweets.addAll(Tweet.fromJSONArray(json));
                myTweet = tweets.get(0);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Log.d("DEBUG", errorResponse.toString());
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar2);
        setSupportActionBar(toolbar);
        if(savedInstanceState == null){
            String screenName = getIntent().getStringExtra("sreen_name");
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
        populateProfileHeader(myTweet);


    }

    private void populateProfileHeader(Tweet tweet) {

        TextView textViewUser = (TextView) findViewById(R.id.textViewUser);
        TextView textViewTagLine = (TextView) findViewById(R.id.textVTagLine);
        TextView textviewFollowers = (TextView) findViewById(R.id.textViewFollower);
        TextView textviewFollowing = (TextView) findViewById(R.id.textViewFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);


        textViewTagLine.setText(tweet.getUserDescription());
        textViewUser.setText(tweet.getScreenName());
        textviewFollowers.setText(tweet.getFollowers() + " followers");
        textviewFollowing.setText(tweet.getFollowing() + " following");

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);

       Glide.with(this).load(tweet.getImageLast()).error(R.drawable.error).placeholder(R.drawable.twtsmall).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(ivProfileImage);
    }


}
