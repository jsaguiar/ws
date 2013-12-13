//
//  PlaceCategory.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/13/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "PlaceCategory.h"

@implementation PlaceCategory
@synthesize name;
@synthesize identifier;

+ (id)PlaceCategoryWihtName:(NSString*)name withIdentifier:(NSString*)identifier{
    PlaceCategory *category = [[self alloc] init];
    category.name = identifier;
    category.identifier = name;
    
    
    return category;
}

@end
