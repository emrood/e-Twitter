package com.codepath.apps.mysimpletweets.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.mysimpletweets.R.id.progressBar;
import static com.codepath.apps.mysimpletweets.R.id.tvBody;
import static com.codepath.apps.mysimpletweets.R.id.tvRetweet;
import static com.codepath.apps.mysimpletweets.R.id.tvUserName;

/**
 * Created by Emmanuel Roodly on 03/08/2017.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {



    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    //setup custom template
    //ViewHolder patern to be implement

    private  static class ViewHolder{
        TextView tvUserName;
        TextView tvBody;
        TextView tvDate;
        TextView tvRetweet;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        final ViewHolder viewHolder;


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvRetweet = (TextView) convertView.findViewById(R.id.tvRetweet);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


       ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivUserPic);
       //ImageView ivTest = (ImageView) convertView.findViewById(R.id.ivTest);
        //TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        //TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        viewHolder.tvRetweet.setVisibility(View.INVISIBLE);
        if(tweet.getNbrRetweet() != 0){
            viewHolder.tvRetweet.setText("Retweet: " + tweet.getNbrRetweet());
            viewHolder.tvRetweet.setVisibility(View.VISIBLE);
        }
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvDate.setText(getRelativeTimeAgo(tweet.getCreateAt()));

        ivProfilePic.setImageResource(android.R.color.transparent);

        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

        //ivTest.setImageResource(android.R.color.transparent);

        //Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).error(R.drawable.error).placeholder(R.drawable.twtsmall).into(ivTest);

        progressBar.setVisibility(View.VISIBLE);
        

        Glide.with(getContext()).load(tweet.getImageLast()).error(R.drawable.error).placeholder(R.drawable.twtsmall).bitmapTransform(new RoundedCornersTransformation(getContext(),30,10), new CropCircleTransformation(getContext())).listener(new RequestListener<String, GlideDrawable>() {
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
        }).into(ivProfilePic);

        return  convertView;

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
