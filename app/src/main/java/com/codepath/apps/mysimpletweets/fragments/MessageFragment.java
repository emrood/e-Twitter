package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Adapters.MessageArrayAdapter;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TimelineActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Message;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Emmanuel Roodly on 13/08/2017.
 */

public class MessageFragment extends DialogFragment {
    FragmentManager fh;
    TwitterClient client;
    DirectMessageFragment sendMessage;
    ImageButton ibNewMessage;
    ImageButton ibExit;
    ArrayList<Message> messages;
    public MessageArrayAdapter messageArrayAdapter;
    private ListView lvMessage;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, null);

        getDialog().setTitle("New Tweet");
        int width= getResources().getDimensionPixelSize(R.dimen.fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.fragment_height);
        getDialog().getWindow().setLayout(width, height);



        lvMessage = (ListView) root.findViewById(R.id.lvMessage);
        lvMessage.setAdapter(messageArrayAdapter);
        ibNewMessage = (ImageButton) root.findViewById(R.id.ibNewMessage);
        ibExit = (ImageButton) root.findViewById(R.id.ibExit);

        ibNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage.show(fh, "sending");
            }
        });

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return  root;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fh = getFragmentManager();
        client = TwitterApplication.getRestClient();
        sendMessage = new DirectMessageFragment();
        messages = new ArrayList<>();
        messageArrayAdapter = new MessageArrayAdapter(getContext(), messages);
        populateMessage();
    }

    public void populateMessage(){
        if(isNetworkAvailable()){
            if(isOnline()){
                client.getDirectMessage(new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        Log.d("DEBUG", json.toString());
                        //ArrayList<Message> ms = Message.fromJSONArray(json);
                        messageArrayAdapter.addAll(Message.fromJSONArray(json));
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }else {
                Toast.makeText(getContext(), "Poor connection", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "Use Wi-fi or Mobile data", Toast.LENGTH_SHORT).show();
        }

    }


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

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
