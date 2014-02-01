package com.freshplanet.twitterapp;

import com.capitrium.twitterauth.TwitterUtil;
import com.capitrium.twitterauth.TwitterValues;

import android.util.Log;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterProxy {
	
	private static AsyncTwitter asyncTwitter;
	
	public TwitterProxy() {
		
	}
	
	private static void initAsyncTwitter() {
		if (asyncTwitter != null) return;
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterValues.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterValues.TWITTER_CONSUMER_SECRET);

		AccessToken accessToken = new AccessToken(
			TwitterValues.TWITTER_OAUTH_TOKEN,
			TwitterValues.TWITTER_OAUTH_TOKEN_SECRET
		);

		asyncTwitter = new AsyncTwitterFactory(builder.build()).getInstance(accessToken);

		TwitterListener listener = new TwitterAdapter() {
			@Override
			public void updatedStatus(Status status) {
				Log.i("Status", "Successfully updated the status to [" +
						status.getText() + "].");
			}

			@Override
			public void onException(TwitterException te, TwitterMethod method) {
				if (method == TwitterMethod.UPDATE_STATUS) {
					Log.i("Status", "We have an exception");
					te.printStackTrace();
				} else {
					throw new AssertionError("Should not happen");
				}
			}
		};
		
		asyncTwitter.addListener(listener);

	}

	
	private static String counter = "poi9";
	public static void updateStatusAsync(String statusString) {
		initAsyncTwitter();
		
		counter = counter + "1";
		statusString = statusString.concat(counter);
		
		asyncTwitter.updateStatus(statusString);
	}
	
	public static void updateStatus(String statusString) {
		// The factory instance is re-usable and thread safe.
		Twitter twitter = TwitterUtil.getInstance().getTwitter();
		if (twitter != null) {
			
			Status status;
			try {
				status = twitter.updateStatus(statusString);
				Log.i("twitter", "Successfully updated the status to [" + status.getText() + "].");
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e("TwitterAuth", "Twitter not been initialized");
		}
	}

}
