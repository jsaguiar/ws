//
//  PlaceCategory.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/13/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PlaceCategory : NSObject{
    NSString *identifier;
    NSString *name;
}

@property (nonatomic, copy) NSString *identifier;
@property (nonatomic, copy) NSString *name;

+ (id)PlaceCategoryWihtName:(NSString*)name withIdentifier:(NSString*)identifier;


@end
