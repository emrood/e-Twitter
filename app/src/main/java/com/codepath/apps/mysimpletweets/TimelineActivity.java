package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionTimeline;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
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

import static com.codepath.apps.mysimpletweets.R.id.lvTweets;
import static com.codepath.apps.mysimpletweets.R.id.swipeContainer;

public class TimelineActivity extends AppCompatActivity{

    private TwitterClient client;
    private TweetsListFragment fragmentTweetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        //Get the VoiewPager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetspagerAdapter(getSupportFragmentManager()));
        //set Adapter to ViewPager

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

    //methode onClick (FAB) faisant appelle au Dialogfragment: Fragmenttweet

    //Methode recevant un signal du tweet envoye | executant un rafraichiisement du timeline apres 1,5s
    public void onResultFromFragment(){
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentTweetList.aTweets.clear();
                //populateTimeline();
            }
        }, 1500);

    }

    //done l'ordre des fragments
    public  class TweetspagerAdapter extends FragmentPagerAdapter{

        final int PAGE_COUNT = 2;
        private String tabTitle[] = {"Home", "Mentions"};

        public TweetspagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new HomeTimelineFragment();
            }else if (position == 1){
                return  new MentionTimeline();
            }else{
                return  null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

}
