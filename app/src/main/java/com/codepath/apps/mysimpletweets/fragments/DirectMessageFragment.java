package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;

import static com.codepath.apps.mysimpletweets.R.id.etNew;
import static com.codepath.apps.mysimpletweets.R.id.ibClose;
import static com.codepath.apps.mysimpletweets.R.id.ibSend;
import static com.codepath.apps.mysimpletweets.R.id.tvCounter;

/**
 * Created by Emmanuel Roodly on 12/08/2017.
 */

public class DirectMessageFragment extends DialogFragment {
    FragmentManager fh;

    Button btSend;
    Button btCancel;
    EditText etReceiver;
    EditText etMessage;

    TwitterClient client;
    TimelineActivity timelineActivity;
    TweetsListFragment tweetsListFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.direct_message_fragment, null);
        getDialog().setTitle("New Tweet");
        int width= getResources().getDimensionPixelSize(R.dimen.fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.fragment_height);
        getDialog().getWindow().setLayout(width, height);
        fh = getFragmentManager();
        timelineActivity = (TimelineActivity) getActivity();
        client = TwitterApplication.getRestClient();
        btSend = (Button) rootView.findViewById(R.id.btSend);
        btCancel = (Button) rootView.findViewById(R.id.btCancel2);
        etReceiver = (EditText) rootView.findViewById(R.id.etReceiver);
        etMessage = (EditText) rootView.findViewById(R.id.etMessage);


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onSendTweet(etNew.getText().toString());//envoie du message direct
                //timelineActivity.onResultFromFragment();//envoie d'un signal a l'activite principale lorsqu'un message est envoiye
                dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return rootView;
    }
}
