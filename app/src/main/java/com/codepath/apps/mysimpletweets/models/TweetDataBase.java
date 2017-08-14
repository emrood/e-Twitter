package com.codepath.apps.mysimpletweets.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Contacts.SettingsColumns.KEY;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.codepath.apps.mysimpletweets.R.drawable.newtweet;

/**
 * Created by Emmanuel Roodly on 13/08/2017.
 */

public class TweetDataBase extends SQLiteOpenHelper {

    private static TweetDataBase sInstance;
    //Information sur la base de donnee
    private static final String DATABASE_NAME = "TweetDataBAse";
    private static final int DATABASE_VERSION = 1;

    //Table de tweets
    private static final String TABLE_TWEET = "TWEETS";

    //Column of table tweets
    private static final String KEY_ID = "id";
    private static final String KEY_TWEET_UID = "tweet_id";
    private static final String KEY_TWEETBODY = "tweet_body";
    private static final String KEY_TWEETSC = "screen_name";
    private static final String KEY_TWEETDATE = "created_at";
    private static final String KEY_RETWEETCOUNT = "retweet";
    private static final String KEY_USERPIC = "user_pic";





    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public TweetDataBase(Context context) {
        super(context, DATABASE_NAME, null ,DATABASE_VERSION);
    }

    //First Creation of the DataBase
    // ID | tweet_uid | user_screen_name | date of creation | Tweet body | retweet count | picture_url
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TWEETS_TABLE = " CREATE TABLE  " + TABLE_TWEET +
                "("+
                    KEY_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    KEY_TWEET_UID + " INTEGER REFERENCES, "+
                    KEY_TWEETSC  + " TEXT, "+
                    KEY_TWEETDATE + " TEXT, " +
                    KEY_TWEETBODY + " TEXT, "+
                    KEY_RETWEETCOUNT + " INTEGER, "+
                    KEY_USERPIC + " TEXT" +
                ")";

        db.execSQL(CREATE_TWEETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(oldVersion != newVersion){
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEET);
                onCreate(db);
            }
    }

    public static synchronized TweetDataBase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TweetDataBase(context.getApplicationContext());
        }
        return sInstance;
    }

    // ID | tweet_uid | user_screen_name | date of creation | Tweet body | retweet count | picture_url
    public void addTweet(Tweet tweet) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).


            ContentValues values = new ContentValues();
            values.put(KEY_TWEET_UID, tweet.getUid());
            values.put(KEY_TWEETSC, tweet.getScreenName());
            values.put(KEY_TWEETDATE, tweet.getCreateAt());
            values.put(KEY_TWEETBODY, tweet.getBody());
            values.put(KEY_RETWEETCOUNT, tweet.getNbrRetweet());
            values.put(KEY_USERPIC, tweet.getImageLast());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TWEET, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addAllTweet(List<Tweet> tweets) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        for(int i = 0; i < tweets.size(); i++){
            Tweet tweet = new Tweet();
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_TWEET_UID, tweet.getUid());
                values.put(KEY_TWEETSC, tweet.getScreenName());
                values.put(KEY_TWEETDATE, tweet.getCreateAt());
                values.put(KEY_TWEETBODY, tweet.getBody());
                values.put(KEY_RETWEETCOUNT, tweet.getNbrRetweet());
                values.put(KEY_USERPIC, tweet.getImageLast());

                // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
                db.insertOrThrow(TABLE_TWEET, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to add post to database");
                continue;
            }
        }
        db.endTransaction();

    }


    public Cursor getTweets() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TWEET;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    // ID | tweet_uid | user_screen_name | date of creation | Tweet body | retweet count | picture_url
    public List<Tweet> getAllTweets() {
        List<Tweet> tweets = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String TWEETS_SELECT_QUERY = String.format("SELECT * FROM " + TABLE_TWEET);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TWEETS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    Tweet tweet = new Tweet();
                    tweet.setUid(cursor.getLong(cursor.getColumnIndex(KEY_TWEET_UID)));
                    tweet.setScreenName(cursor.getString(cursor.getColumnIndex(KEY_TWEETSC)));
                    tweet.setBody(cursor.getString(cursor.getColumnIndex(KEY_TWEETBODY)));
                    tweet.setCreateAt(cursor.getString(cursor.getColumnIndex(KEY_TWEETDATE)));
                    tweet.setNbrRetweet(cursor.getInt(cursor.getColumnIndex(KEY_RETWEETCOUNT)));
                    tweet.setImageLast(cursor.getString(cursor.getColumnIndex(KEY_USERPIC)));

                    tweets.add(tweet);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tweets;
    }
}

