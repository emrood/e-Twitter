package com.codepath.apps.mysimpletweets.models;

import android.app.Activity;
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
import com.loopj.android.http.AsyncHttpResponseHandler;

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
    int max = 140;
    TwitterClient client;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tweet_fragment, null);
        getDialog().setTitle("New Tweet");
        int width= getResources().getDimensionPixelSize(R.dimen.fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.fragment_height);
        getDialog().getWindow().setLayout(width, height);
        fh = getFragmentManager();
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
                tvCounter.setText("Caracteres: " + String.valueOf(max - etNew.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


}
