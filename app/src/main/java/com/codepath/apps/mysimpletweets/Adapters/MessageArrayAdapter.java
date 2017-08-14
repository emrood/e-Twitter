package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Message;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Emmanuel Roodly on 13/08/2017.
 */

//Adapter liant la liste des messages direct au List view

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    TwitterClient client;

    public MessageArrayAdapter(Context context, List<Message> messages){
        super(context, 0, messages);
    }

    public static class ViewHolder{
        TextView tvSN; //Screen_name de l'autre utilisateur
        TextView tvDateMessage;//Date du message
        TextView tvMessageBody;//corps du message
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message message = getItem(position);

        final ViewHolder viewHolder;

        client = TwitterApplication.getRestClient();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvSN = (TextView) convertView.findViewById(R.id.tvSN);
            viewHolder.tvDateMessage = (TextView) convertView.findViewById(R.id.tvDateMessage);
            viewHolder.tvMessageBody = (TextView) convertView.findViewById(R.id.tvMessageBody);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageView ivProfileMessage = (ImageView) convertView.findViewById(R.id.ivProfilMessage);
        ivProfileMessage.setImageResource(android.R.color.transparent);

        viewHolder.tvSN.setText(message.getScreenName());
        viewHolder.tvMessageBody.setText(message.getBody());
        viewHolder.tvDateMessage.setText(getRelativeTimeAgo(message.getDate()));

        Glide.with(getContext()).load(message.getProfilepic()).into(ivProfileMessage);




        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
