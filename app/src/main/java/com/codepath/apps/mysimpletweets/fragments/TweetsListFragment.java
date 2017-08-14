package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.FragmentTweet;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.swipeContainer;

/**
 * Created by Emmanuel Roodly on 08/08/2017.
 */


public class TweetsListFragment extends Fragment {



    private ArrayList<Tweet> tweets;
    public TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    public SwipeRefreshLayout swipeContainer;
    public ImageButton reTweet;
    TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        reTweet = (ImageButton) v.findViewById(R.id.ivRetweet);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //loadMoreTimeline();
                return false;
            }
        });

        lvTweets.setAdapter(aTweets);

        lvTweets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                client.onRetweet(tweets.get(position).getUid(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
                return true;
            }
        });



        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Swipe refresh selon le fragment en cour d'utilisation
                if(TweetsListFragment.this instanceof HomeTimelineFragment){
                    ((HomeTimelineFragment) TweetsListFragment.this).aTweets.clear();
                    ((HomeTimelineFragment) TweetsListFragment.this).populateTimeline();
                }else if(TweetsListFragment.this instanceof MentionTimeline){
                    ((MentionTimeline) TweetsListFragment.this).aTweets.clear();
                    ((MentionTimeline) TweetsListFragment.this).populateTimeline();
                }else if(TweetsListFragment.this instanceof UserTimelineFragment){
                    ((UserTimelineFragment) TweetsListFragment.this).aTweets.clear();
                    ((UserTimelineFragment) TweetsListFragment.this).populateTimeline();
                }

            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

    }





    public  void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
        swipeContainer.setRefreshing(false);
    }

    //Methode testant la connection internet
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //methode testant si une connection est active
    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
