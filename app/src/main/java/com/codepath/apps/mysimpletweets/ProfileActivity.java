package com.codepath.apps.mysimpletweets;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.progressBar;
import static com.codepath.apps.mysimpletweets.TwitterApplication.getRestClient;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                user = User.fromJSON(response);// my curretnt user account info
                getSupportActionBar().setTitle("e-Twitter | @" + user.getName());
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

        populateProfileHeader(user);

    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvUsername);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagLine);
        ImageView ivUserpicture = (ImageView) findViewById(R.id.ivUserPicture);
        TextView tvFollower = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        tvTagline.setText(user.getTagLine());
        tvName.setText(user.getName());
        tvFollower.setText(user.getFollower() + " followers");
        tvFollowing.setText(user.getFollowing() + " following");

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(this).load(user.getProfileImageUrl()).error(R.drawable.error).placeholder(R.drawable.twtsmall).listener(new RequestListener<String, GlideDrawable>() {
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
        }).into(ivUserpicture);
    }


}
