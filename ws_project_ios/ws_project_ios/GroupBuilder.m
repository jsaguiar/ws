//
//  GroupBuilder.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "GroupBuilder.h"
#import "PlaceCategory.h"
#import "Place.h"
@implementation GroupBuilder

+ (NSArray *)placesFromJSON:(NSData *)objectNotation error:(NSError **)error{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    
    NSMutableArray *groups = [[NSMutableArray alloc] init];
    
    NSArray *results = [parsedObject valueForKey:@"results"];
    results =[results valueForKey:@"bindings"];
    
    for (NSDictionary *groupDic in results) {
        Place *place = [[Place alloc] init];
        
        NSDictionary *group1 = [groupDic valueForKey:@"name"];
        NSString *result=[group1 valueForKey:@"value"];
        [place setValue:result forKey:@"name"];

        group1 = [groupDic valueForKey:@"id"];
        result=[group1 valueForKey:@"value"];
        [place setValue:result forKey:@"identifier"];

        
        group1 = [groupDic valueForKey:@"lat"];
        result=[group1 valueForKey:@"value"];
        [place setValue:result forKey:@"lat"];
        
        
        group1 = [groupDic valueForKey:@"lng"];
        result=[group1 valueForKey:@"value"];
        [place setValue:result forKey:@"lng"];
       
        [groups addObject:place];
        
    }
    
    NSArray *sortedArray;
    sortedArray = [groups sortedArrayUsingComparator:^NSComparisonResult(id a, id b) {
        NSString *first = [(PlaceCategory*)a name];
        NSString *second = [(PlaceCategory*)b name];
        return [first compare:second];
    }];
    return sortedArray ;

    
}


+ (NSArray *)categoriesFromJSON:(NSData *)objectNotation error:(NSError **)error
{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    
    NSMutableArray *groups = [[NSMutableArray alloc] init];
    
    NSArray *results = [parsedObject valueForKey:@"results"];
    results =[results valueForKey:@"bindings"];
    
    for (NSDictionary *groupDic in results) {
        PlaceCategory *group = [[PlaceCategory alloc] init];
        
        NSDictionary *group1 = [groupDic valueForKey:@"category"];
        NSString *received=[group1 valueForKey:@"value"];
        
        NSArray *aux=[received componentsSeparatedByString:@"#"];
        if ([aux count] >= 1 ){
            [group setValue:aux[1] forKey:@"name" ];
            
            group1 = [groupDic valueForKey:@"LABEL"];
            received=[group1 valueForKey:@"value"];
            [group setValue:received forKey:@"label" ];
            
            
            group1 = [groupDic valueForKey:@"instances_count"];
            received= [group1 valueForKey:@"value"] ;
            [group setValue:received forKey:@"childNum" ];
            
            [groups addObject:group];
        }
        

    }
    
    NSArray *sortedArray;
    sortedArray = [groups sortedArrayUsingComparator:^NSComparisonResult(id a, id b) {
        NSString *first = [(PlaceCategory*)a name];
        NSString *second = [(PlaceCategory*)b name];
        return [first compare:second];
    }];
    
    NSLog(@"Categories Received %d",[groups count]);
    return sortedArray ;
}
@end
