//
//  FPViewController.m
//  TwitterApp-ios
//
//  Created by Nigam Shah on 2/2/14.
//  Copyright (c) 2014 Fresh Planet. All rights reserved.
//

#import "FPViewController.h"
#import "FPTwitterProxy.h"

@interface FPViewController ()

@end

@implementation FPViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
	
	NSLog(@"at viewDidLoad");
	
	[FPTwitterProxy setViewController:self];
	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onTweetClick:(id)sender {
	[FPTwitterProxy updateStatusWithTweetSheet:@"This is tweet test 1 on superbowl sunday"];
}

- (IBAction)onRetweetClick:(id)sender {
	NSLog(@"onRetweetClick");
	[FPTwitterProxy retweetStatus:@"430060763621756928"];

}

- (IBAction)onFollowClick:(id)sender {
	NSLog(@"onFollowClick");
}

- (IBAction)onFavoriteClick:(id)sender {
	NSLog(@"onFavoriteClick");
}

- (IBAction)onReplyClick:(id)sender {
	NSLog(@"onReplyClick");
}
@end

