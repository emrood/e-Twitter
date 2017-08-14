package com.codepath.apps.mysimpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	//public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "eqKprX0QrIg09FkBu5kwu7pYA";       // Change this
	public static final String REST_CONSUMER_SECRET = "2Tdfcx3QcizYqbuXzDH9Cs9tn51r4yC0JvCgdKBdABkJEP9XF4"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://e-Twitter"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}



	public void getHomeTimeline(AsyncHttpResponseHandler handler){
		String url = getApiUrl("statuses/home_timeline.json");
		//mise en place des parametres de la requete: nbe de tweets, ect
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		//ececuter la requete
		getClient().get(url, params, handler);
	}

	//Surcharge de la methode getHomeTimeLine permettant de personnaliser le Tweet de depart
	public void getHomeTimeline(int since, AsyncHttpResponseHandler handler){
        String url = getApiUrl("statuses/home_timeline.json");
        //mise en place des parametres de la requete: nbe de tweets, ect
        RequestParams params = new RequestParams();
        params.put("count", 30);
        params.put("since_id", since);
        //ececuter la requete
        getClient().get(url, params, handler);
    }



	//Methode permettant de poster un tweet
	public void composeATweet(String status, AsyncHttpResponseHandler handler){
		String url = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		getClient().post(url, params, handler);
	}

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String url = getApiUrl("statuses/mentions_timeline.json");
		//mise en place des parametres de la requete: nbe de tweets, ect
		RequestParams params = new RequestParams();
		params.put("count", 25);
		//params.put("since_id", 1);
		//ececuter la requete
		getClient().get(url, params, handler);

	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler){
		String url = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screenName);
		getClient().get(url, params, handler);
	}

	//Methode recevant les info de l'utilisateur en cours
	public void getUserInfo(AsyncHttpResponseHandler handler){
		String url = getApiUrl("account/verify_credentials.json");
		getClient().get(url, null, handler);
	}
 /// Methode de retweet
	public void onRetweet(long id, AsyncHttpResponseHandler handler){
		String url = getApiUrl("statuses/retweet/:id.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(url, params, handler);
	}

	//methode recevant le profil de n'importe quel utilisateur
	public  void getUserProfil(long id, String screenName, AsyncHttpResponseHandler handler){
		String url = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", id);
		params.put("screen_name", screenName);
		getClient().get(url, params, handler);
	}

	//Methode d'envoie d'un message directe
	public  void sendDirectMessage(String screenName, String data, AsyncHttpResponseHandler handler){
		String url = getApiUrl("direct_messages/new.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		params.put("text", data);
		getClient().post(url, params, handler);
	}

	//Methode de reception de la liste des messages directes
	public void getDirectMessage(AsyncHttpResponseHandler handler){
		String url = getApiUrl("direct_messages.json");
		getClient().get(url, null, handler);
	}
}