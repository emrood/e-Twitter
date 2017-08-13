package com.codepath.apps.mysimpletweets;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {

    public TwitterClient client;
    public User user;
    public JSONObject json;
    public TextView textViewUser;
    public TextView textViewTagLine;
    public TextView textviewFollowers;
    public TextView textviewFollowing;
    public ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        client = TwitterApplication.getRestClient();

        textViewUser = (TextView) findViewById(R.id.textViewUser2);
        textViewTagLine = (TextView) findViewById(R.id.textVTagLine2);
        textviewFollowers = (TextView) findViewById(R.id.textViewFollower2);
        textviewFollowing = (TextView) findViewById(R.id.textViewFollowing2);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage2);

        String screen = getIntent().getStringExtra("screenN");
        long userid = getIntent().getLongExtra("id", -1);
        user = new User();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar22);
        progressBar.setVisibility(View.VISIBLE);
        client.getUserProfil(userid, screen, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    textViewTagLine.setText(response.getString("description"));
                    textViewUser.setText(response.getString("name"));
                    textviewFollowers.setText(response.getInt("followers_count") + " followers");
                    textviewFollowing.setText(response.getInt("friends_count") + " following");
                    getSupportActionBar().setTitle("e-Twitter | @" + response.getString("screen_name"));
                    Glide.with(getBaseContext()).load(response.getString("profile_image_url")).error(R.drawable.error).placeholder(R.drawable.twtsmall).listener(new RequestListener<String, GlideDrawable>() {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar2);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            String screenName = getIntent().getStringExtra("sreen_name");
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screen);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer2, fragmentUserTimeline);
            ft.commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public void onReturn(MenuItem item) {
        finish();
    }

    public void onDisconnect(MenuItem item) {
        client.clearAccessToken();
        finish();
    }
}
