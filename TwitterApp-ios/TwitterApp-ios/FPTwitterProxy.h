//
//  FPTwitterProxy.h
//  TwitterApp-ios
//
//  Created by Nigam Shah on 2/2/14.
//  Copyright (c) 2014 Fresh Planet. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Accounts/Accounts.h>
#import "FPViewController.h"


typedef void (^TwitterAPISuccessHandler)(NSDictionary*);
typedef void (^TwitterAccountRequestCompleteHander)(ACAccount*);

@interface FPTwitterProxy : NSObject

+ (void) setViewController:(UIViewController *)val;

+ (BOOL) userHasAccessToTwitter;

+ (void) updateStatusWithTweetSheet:(NSString *)message;

+ (void) updateStatusInReplyToStatus:(NSString *)statusId;

+ (void) retweetStatus:(NSString *)statusId;

+ (void) favoriteStatus:(NSString *)statusId;

+ (void) createFriendship:(NSString *)userId;

+ (void) destroyFriendship:(NSString *)userId;

@end