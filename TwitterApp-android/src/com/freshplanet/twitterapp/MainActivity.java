package com.freshplanet.twitterapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capitrium.twitterauth.TwitterTasks;
import com.capitrium.twitterauth.TwitterValues;

public class MainActivity extends Activity {
	
	private TextView tv;
	private Button tweetButton;
	private Button retweetButton;
	private Button followButton;
	private Button unfollowButton;
	
	private String counter = "c";
	private long retweetId = 428891293255610368L;
	private long followId = 14075928L;
	
	public View.OnClickListener tweetButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			counter += "1";
			TwitterProxy.updateStatus("watching L&O: " + counter, MainActivity.this);
		}
	};
	public View.OnClickListener retweetButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TwitterProxy.retweetStatus(retweetId, MainActivity.this);
		}
	};
	public View.OnClickListener followButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TwitterProxy.createFriendship(followId, MainActivity.this);
		}
	};

	public View.OnClickListener unfollowButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TwitterProxy.destroyFriendship(followId, MainActivity.this);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.textview);

		tweetButton = (Button) findViewById(R.id.tweetButton);
		tweetButton.setOnClickListener(tweetButtonListener);
		
		retweetButton = (Button) findViewById(R.id.retweetButton);
		retweetButton.setOnClickListener(retweetButtonListener);
		
		followButton = (Button) findViewById(R.id.followButton);
		followButton.setOnClickListener(followButtonListener);
		
		unfollowButton = (Button) findViewById(R.id.unfollowButton);
		unfollowButton.setOnClickListener(unfollowButtonListener);
		
		processTwitterLogin();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(TwitterValues.TWITTER_IS_LOGGED_IN) {
			tv.setText("Hello, " + TwitterValues.TWITTER_USER.getName() + "!");
			TwitterProxy.doCommand();
		} else {
			tv.setText("No user is currently logged in...");
		}
	}
	
	public void processTwitterLogin() {
		Uri uri = getIntent().getData();
		if(uri != null) Log.i("MainActivity", "returned url from twitter: " + uri.toString());
		if(uri != null && uri.toString().startsWith(TwitterValues.TWITTER_CALLBACK_URL)) {
			Log.i("Twitter", "uri contains oauth_verifier, = " + uri.toString());
			String verifier = uri.getQueryParameter(TwitterValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
			new TwitterTasks.TwitterGetAccessTokenTask(MainActivity.this, MainActivity.this).execute(verifier);
		}
	}
}