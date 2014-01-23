//
//  Place.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/16/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Place : NSObject

@property (nonatomic, copy) NSString *identifier;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *lat;
@property (nonatomic, copy) NSString *lng;
@property (nonatomic, copy) NSString *contact;
@property (nonatomic, copy) NSString *address;
@property (nonatomic, copy) NSNumber *price;
@property (nonatomic, copy) NSNumber *rating;
@property (nonatomic, copy) NSString *descrip;

@end
