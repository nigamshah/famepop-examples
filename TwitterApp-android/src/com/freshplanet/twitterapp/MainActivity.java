package com.freshplanet.twitterapp;

import twitter4j.TwitterException;
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
	private Button loginButton;
	private Button tweetButton;
	
	
	public View.OnClickListener loginButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(!TwitterValues.TWITTER_IS_LOGGED_IN) {
				new TwitterTasks.TwitterGetRequestTokenTask(MainActivity.this).execute();
			} else {
				TwitterTasks.logoutFromTwitter();
				tv.setText("No user is currently logged in...");
				loginButton.setText("Login");
			}
		}
	};
	public View.OnClickListener tweetButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(TwitterValues.TWITTER_IS_LOGGED_IN) {
				TwitterProxy.updateStatusAsync("This is status update 1");
			}
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.textview);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginButtonListener);
		tweetButton = (Button) findViewById(R.id.tweetButton);
		tweetButton.setOnClickListener(tweetButtonListener);
		tweetButton.setEnabled(false);
		processTwitterLogin();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		changeLoginButton();
		tweetButton.setEnabled(true);
	}
	
	public void processTwitterLogin() {
		Uri uri = getIntent().getData();
		if(uri != null) Log.i("MainActivity", "returned url from twitter: " + uri.toString());
		if(uri != null && uri.toString().startsWith(TwitterValues.TWITTER_CALLBACK_URL)) {
			Log.i("MainActivity", "uri contains oauth_verifier");
			String verifier = uri.getQueryParameter(TwitterValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
			new TwitterTasks.TwitterGetAccessTokenTask(MainActivity.this, MainActivity.this).execute(verifier);
		}
	}
	
	public void changeLoginButton() {
		if(TwitterValues.TWITTER_IS_LOGGED_IN) {
			tv.setText("Hello, " + TwitterValues.TWITTER_USER.getName() + "!");
			loginButton.setText("Logout");
		} else {
			tv.setText("No user is currently logged in...");
			loginButton.setText("Login");
		}
	}
}