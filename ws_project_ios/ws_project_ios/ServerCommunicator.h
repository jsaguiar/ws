//
//  ServerCommunicator.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ServerCommunicatorDelegate.h"
#import <CoreLocation/CoreLocation.h>


@protocol ServerCommunicatorDelegate;

@interface ServerCommunicator : NSObject
@property (weak, nonatomic) id<ServerCommunicatorDelegate> delegate;

- (void)getCategories: (NSString*) category;
- (void)getPlacesForCategory: (NSArray*) category;

@end
