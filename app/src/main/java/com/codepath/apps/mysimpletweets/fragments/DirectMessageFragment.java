package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.etNew;
import static com.codepath.apps.mysimpletweets.R.id.ibClose;
import static com.codepath.apps.mysimpletweets.R.id.ibSend;
import static com.codepath.apps.mysimpletweets.R.id.tvCounter;

/**
 * Created by Emmanuel Roodly on 12/08/2017.
 */

//Fragment d'envoie de message Directe a un utilisateur via son screen_name

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

        btSend = (Button) rootView.findViewById(R.id.btSend);//Boutton envoye
        btCancel = (Button) rootView.findViewById(R.id.btCancel2);//boutton cancel

        etReceiver = (EditText) rootView.findViewById(R.id.etReceiver);//champs recevant le screen name du destinataire
        etMessage = (EditText) rootView.findViewById(R.id.etMessage);//Champs recevant le corps du message


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Test de champs null avant d'envoye un message
                if(TextUtils.isEmpty(etReceiver.getText().toString()) || TextUtils.isEmpty(etMessage.getText().toString())){
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else{
                    onSendMessage(etReceiver.getText().toString(), etMessage.getText().toString());//envoie du message si aucun champs n'est nul
                    dismiss();
                }

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

    //Methode permettant d'envoyer un message directe
    public  void onSendMessage(String screenname, String message){
        client.sendDirectMessage(screenname, message, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
