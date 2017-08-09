package com.codepath.apps.mysimpletweets.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Emmanuel Roodly on 04/08/2017.
 */

public class FragmentTweet extends DialogFragment {
    FragmentManager fh;

    ImageButton ibSend;
    ImageButton ibClose;
    EditText etNew;
    TextView tvCounter;
    int max = 140;//variable comptenant la valeur max de caracteres permise
    TwitterClient client;
    TimelineActivity timelineActivity;
    TweetsListFragment tweetsListFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tweet_fragment, null);
        getDialog().setTitle("New Tweet");
        int width= getResources().getDimensionPixelSize(R.dimen.fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.fragment_height);
        getDialog().getWindow().setLayout(width, height);
        fh = getFragmentManager();
        timelineActivity = (TimelineActivity) getActivity();
        client = TwitterApplication.getRestClient();
        ibSend = (ImageButton) rootView.findViewById(R.id.ibSend);
        etNew = (EditText) rootView.findViewById(R.id.etNew);
        ibClose = (ImageButton) rootView.findViewById(R.id.ibClose);
        tvCounter = (TextView) rootView.findViewById(R.id.tvCounter);
        etNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvCounter.setText("Caracteres: " + String.valueOf(max - etNew.getText().length()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCounter.setText("Caracteres: " + String.valueOf(max - etNew.getText().length()));//compteur pour restriction a 140 caracteres
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendTweet(etNew.getText().toString());//envoie du tweet
                timelineActivity.onResultFromFragment();//envoie d'un signal a l'activite principale lorsqu'un tweet est envoiye
                dismiss();
            }
        });

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return rootView;
    }

    //methode postant le tweet
    private void onSendTweet(String message){
        client.composeATweet(message, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(getActivity());
    }
}
