//
//  FPTwitterProxy.m
//  TwitterApp-ios
//
//  Created by Nigam Shah on 2/2/14.
//  Copyright (c) 2014 Fresh Planet. All rights reserved.
//

#import "FPTwitterProxy.h"

#import <Social/Social.h>
#import <Accounts/Accounts.h>

@implementation FPTwitterProxy

static UIViewController * viewController;
static ACAccount* twitterAccount = nil;

+ (void) setViewController:(UIViewController *) val {
	@synchronized(self) { viewController = val; };
}


+ (void) updateStatusWithTweetSheet:(NSString *)message {
	if ([FPTwitterProxy userHasAccessToTwitter]) {
		SLComposeViewController *tweetSheet = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
		[tweetSheet setInitialText:message];
		[viewController presentViewController:tweetSheet animated:YES completion:nil];
	} else {
		[FPTwitterProxy showNoTwitterAlert];
	}
}

+ (void) updateStatusInReplyToStatus:(NSString *)statusId {
	
}

+ (void) retweetStatus:(NSString *)statusId {
	
	NSString *urlToAppend = [NSString stringWithFormat:@"statuses/retweet/%@.json", statusId];
	[FPTwitterProxy executeTwitterAPICall:SLRequestMethodPOST
							  urlToAppend:urlToAppend
								   params:nil
						   successHandler:^(NSDictionary *responseData) {
							  
							   NSLog(@"YEAH\n%@", responseData);
							   
						   }];

}

+ (void) favoriteStatus:(NSString *)statusId {
	NSString *urlToAppend = @"favorites/create.json";
	NSDictionary *params = @{@"id": statusId};
	
	[FPTwitterProxy executeTwitterAPICall:SLRequestMethodPOST
							  urlToAppend:urlToAppend
								   params:params
						   successHandler:^(NSDictionary *responseData) {
							   NSLog(@"YEAH\n%@", responseData);
						   }];
}

+ (void) createFriendship:(NSString *)userId {
	NSString *urlToAppend = @"friendships/create.json";
	NSDictionary *params = @{@"user_id": userId, @"follow": @"true"};
	
	[FPTwitterProxy executeTwitterAPICall:SLRequestMethodPOST
							  urlToAppend:urlToAppend
								   params:params
						   successHandler:^(NSDictionary *responseData) {
							   NSLog(@"YEAH\n%@", responseData);
						   }];
}

+ (void) destroyFriendship:(NSString *)userId {
	NSString *urlToAppend = @"friendships/destroy.json";
	NSDictionary *params = @{@"user_id": userId};
	
	[FPTwitterProxy executeTwitterAPICall:SLRequestMethodPOST
							  urlToAppend:urlToAppend
								   params:params
						   successHandler:^(NSDictionary *responseData) {
							   NSLog(@"YEAH\n%@", responseData);
						   }];
}

// helper methods

+ (void) initTwitterAccount:(TwitterAccountRequestCompleteHander)onComplete {

	if (twitterAccount != nil) {
		onComplete(twitterAccount);
		return;
	}
	
	ACAccountStore *accountStore = [[ACAccountStore alloc] init];
	ACAccountType *twitterAccountType =	[accountStore accountTypeWithAccountTypeIdentifier: ACAccountTypeIdentifierTwitter];
	[accountStore requestAccessToAccountsWithType:twitterAccountType options:NULL completion:^(BOOL granted, NSError *error) {
		if (granted) {
			NSArray *twitterAccounts = [accountStore accountsWithAccountType:twitterAccountType];
			twitterAccount = [twitterAccounts lastObject];
		} else {
			// TODO: Handle case where user did not grant permission
		}
		if (onComplete != nil) {
			onComplete(twitterAccount);
		}
	}];
}

+ (void) executeTwitterAPICall:(SLRequestMethod)method
				   urlToAppend:(NSString *)urlToAppend
						params:(NSDictionary *)params
				successHandler:(TwitterAPISuccessHandler)successHandler {
	
	[FPTwitterProxy initTwitterAccount:^(ACAccount *account) {
		
		if (account != nil) {
			NSString *urlString = [NSString stringWithFormat:@"https://api.twitter.com/1.1/%@", urlToAppend];
			
			NSURL *url = [NSURL URLWithString:urlString];
			
			SLRequest *request = [SLRequest requestForServiceType:SLServiceTypeTwitter
													requestMethod:method
															  URL:url
													   parameters:params];
			//  Attach an account to the request
			[request setAccount:twitterAccount];
			
			// Execute the request
			[request performRequestWithHandler:^(NSData *responseData,
												 NSHTTPURLResponse *urlResponse,
												 NSError *error) {
				if (responseData) {
					if (urlResponse.statusCode >= 200 && urlResponse.statusCode < 300) {
						NSError *jsonError;
						NSDictionary *responseDataDictionary =
						[NSJSONSerialization JSONObjectWithData:responseData
														options:NSJSONReadingAllowFragments
														  error:&jsonError];

						
						if (responseDataDictionary) {
							NSLog(@"Response: %@\n", responseDataDictionary);
							successHandler(responseDataDictionary);
						} else {
							// Our JSON deserialization went awry
							NSLog(@"JSON Error: %@", [jsonError localizedDescription]);
						}
					} else {
						// The server did not respond ... were we rate-limited?
						NSLog(@"The response status code is %d", urlResponse.statusCode);
					}
				}
			}];
		}
	}];
}

//- (void)fetchTimelineForUser:(NSString *)username
//{
//	//  Step 0: Check that the user has local Twitter accounts
//	if ([self userHasAccessToTwitter]) {
//		
//		//  Step 1:  Obtain access to the user's Twitter accounts
//		ACAccountType *twitterAccountType =
//		[self.accountStore accountTypeWithAccountTypeIdentifier:
//		 ACAccountTypeIdentifierTwitter];
//		
//		[self.accountStore
//		 requestAccessToAccountsWithType:twitterAccountType
//		 options:NULL
//		 completion:^(BOOL granted, NSError *error) {
//			 if (granted) {
//				 //  Step 2:  Create a request
//				 NSArray *twitterAccounts =
//				 [self.accountStore accountsWithAccountType:twitterAccountType];
//				 NSURL *url = [NSURL URLWithString:@"https://api.twitter.com"
//							   @"/1.1/statuses/user_timeline.json"];
//				 NSDictionary *params = @{@"screen_name" : username,
//										  @"include_rts" : @"0",
//										  @"trim_user" : @"1",
//										  @"count" : @"1"};
//				 SLRequest *request =
//				 [SLRequest requestForServiceType:SLServiceTypeTwitter
//									requestMethod:SLRequestMethodGET
//											  URL:url
//									   parameters:params];
//				 
//				 //  Attach an account to the request
//				 [request setAccount:[twitterAccounts lastObject]];
//				 
//				 //  Step 3:  Execute the request
//				 [request performRequestWithHandler:
//				  ^(NSData *responseData,
//					NSHTTPURLResponse *urlResponse,
//					NSError *error) {
//					  
//					  if (responseData) {
//						  if (urlResponse.statusCode >= 200 &&
//							  urlResponse.statusCode < 300) {
//							  
//							  NSError *jsonError;
//							  NSDictionary *timelineData =
//							  [NSJSONSerialization
//							   JSONObjectWithData:responseData
//							   options:NSJSONReadingAllowFragments error:&jsonError];
//							  if (timelineData) {
//								  NSLog(@"Timeline Response: %@\n", timelineData);
//							  }
//							  else {
//								  // Our JSON deserialization went awry
//								  NSLog(@"JSON Error: %@", [jsonError localizedDescription]);
//							  }
//						  }
//						  else {
//							  // The server did not respond ... were we rate-limited?
//							  NSLog(@"The response status code is %d",
//									urlResponse.statusCode);
//						  }
//					  }
//				  }];
//			 }
//			 else {
//				 // Access was not granted, or an error occurred
//				 NSLog(@"%@", [error localizedDescription]);
//			 }
//		 }];
//	}
//}




+ (BOOL) userHasAccessToTwitter {
	BOOL result = [SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter];
	return result;
}

+ (void) showNoTwitterAlert {
	UIAlertView *alertView = [[UIAlertView alloc]
							  initWithTitle:@"Sorry"
							  message:@"You can't send a tweet right now, make sure your device has an internet connection nd you have at least one Twitter account setup"
							  delegate:viewController
							  cancelButtonTitle:@"OK"
							  otherButtonTitles:nil];
	[alertView show];

}
@end