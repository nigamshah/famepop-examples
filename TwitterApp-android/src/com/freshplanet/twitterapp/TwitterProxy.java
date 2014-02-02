package com.freshplanet.twitterapp;

import com.capitrium.twitterauth.TwitterTasks;
import com.capitrium.twitterauth.TwitterValues;

import android.app.Activity;
import android.util.Log;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterProxy {

	interface TwitterAPICommand {
		void execute();
	}
	
	private static AsyncTwitter asyncTwitter;
	private static TwitterAPICommand command;

	public TwitterProxy() {}

	
	public static void updateStatus(final String statusMessage, Activity caller) {
		command = null;
		if(TwitterValues.TWITTER_IS_LOGGED_IN) {
			updateStatusAsync(statusMessage);
		} else {
			command = new TwitterAPICommand() {
				@Override
				public void execute() {
					Log.i("TwitterStatus", "about to update status");
					updateStatusAsync(statusMessage);
				}
			};
			login(caller);
		}
	}
	private static void updateStatusAsync(String statusString) {
		initAsyncTwitter();
		asyncTwitter.updateStatus(statusString);
	}
	
	public static void retweetStatus(final long statusId, Activity caller) {
		//void retweetStatus(long statusId)
		command = null;
		if(TwitterValues.TWITTER_IS_LOGGED_IN) {
			retweetStatusAsync(statusId);
		} else {
			command = new TwitterAPICommand() {
				@Override
				public void execute() {
					Log.i("TwitterStatus", "about to retweet status id = " + statusId);
					retweetStatusAsync(statusId);
				}
			};
			login(caller);
		}
	}
	private static void retweetStatusAsync(long statusId) {
		initAsyncTwitter();
		asyncTwitter.retweetStatus(statusId);
	}
	
	public static void createFriendship(final long userId, Activity caller) {
		//void retweetStatus(long statusId)
		command = null;
		if(TwitterValues.TWITTER_IS_LOGGED_IN) {
			createFriendshipAsync(userId);
		} else {
			command = new TwitterAPICommand() {
				@Override
				public void execute() {
					Log.i("TwitterStatus", "about to friend id = " + userId);
					createFriendshipAsync(userId);
				}
			};
			login(caller);
		}
	}
	private static void createFriendshipAsync(long userId) {
		initAsyncTwitter();
		asyncTwitter.createFriendship(userId, true);
	}

	public static void destroyFriendship(final long userId, Activity caller) {
		command = null;
		if(TwitterValues.TWITTER_IS_LOGGED_IN) {
			destroyFriendshipAsync(userId);
		} else {
			command = new TwitterAPICommand() {
				@Override
				public void execute() {
					Log.i("TwitterStatus", "about to unfriend id = " + userId);
					destroyFriendshipAsync(userId);
				}
			};
			login(caller);
		}
	}
	private static void destroyFriendshipAsync(long userId) {
		initAsyncTwitter();
		asyncTwitter.destroyFriendship(userId);
	}
	
	public static void doCommand() {
		if(command != null) {
			command.execute();
			command = null;
			
			// TODO: send a message to ActionScript
		}
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
				Log.i("TwitterStatus", "Successfully updated the status to [" + status.getText() + "].");
			}
			
			@Override
		    public void retweetedStatus(Status retweetedStatus) {
				Log.i("TwitterStatus", "Successfully retweeted the status [" + retweetedStatus.getText() + "].");
		    }


			@Override
			public void onException(TwitterException te, TwitterMethod method) {
				if (method == TwitterMethod.UPDATE_STATUS) {
					Log.i("Twitter", "We have an exception");
					te.printStackTrace();
				} else {
					throw new AssertionError("Should not happen");
				}
			}
		};
		asyncTwitter.addListener(listener);
	}

	private static void login(Activity caller) {
		// TODO: check for Internet connection
		if(!TwitterValues.TWITTER_IS_LOGGED_IN) {
			new TwitterTasks.TwitterGetRequestTokenTask(caller).execute();
		}
	}
}