package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.FragmentTweet;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.apps.mysimpletweets.R.id.swipeContainer;

/**
 * Created by Emmanuel Roodly on 08/08/2017.
 */


public class TweetsListFragment extends Fragment {


    private ArrayList<Tweet> tweets;
    public TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    FragmentTweet tweety;
    FragmentManager fm;
    public SwipeRefreshLayout swipeContainer;
    private ImageButton reTweet;

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

        //methode pour rafraichir la page (when scrolling up)
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                //populateTimeline();
            }
        });
        lvTweets.setAdapter(aTweets);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        fm = getFragmentManager();
        tweety = new FragmentTweet();
    }

    public void onNewTweet(View view) {
        tweety.show(fm, "New Tweet");

    }



    public  void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }


}
