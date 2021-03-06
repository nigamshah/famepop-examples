package com.capitrium.twitterauth;

import twitter4j.User;

/**
 * Holds the consumer key/secret used to connect with an app registered
 * on Twitter, as well as a callback url containing the scheme and host
 * values that must be registered on the Activity that is launched when
 * Twitter sends back the response to the authentication request.
 */
public class TwitterValues {
	public static String TWITTER_CONSUMER_KEY = "n1Ujbl6r4KeG6HPi0iWxA";
	public static String TWITTER_CONSUMER_SECRET = "7J62QcRyj6DK3dXvy5cMQpbDHtYihpxL7qidobzNE";
	public static String TWITTER_CALLBACK_URL = "oauth://twitter.login";
	public static String URL_PARAMETER_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

	public static String TWITTER_OAUTH_TOKEN = "";
	public static String TWITTER_OAUTH_TOKEN_SECRET = "";
	public static boolean TWITTER_IS_LOGGED_IN = false;
	public static boolean TWITTER_HAS_AUTHORIZED_APP = false;
	
	public static User TWITTER_USER = null;
}
