//
//  GroupBuilder.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GroupBuilder : NSObject
+ (NSArray *)categoriesFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (NSArray *)placesFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (NSArray *)searchFromJSON:(NSData *)objectNotation error:(NSError **)error;

@end
