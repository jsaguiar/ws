//
//  ServerCommunicatorDelegate.h
//  
//
//  Created by Joao Aguiar on 12/14/13.
//
//

#import <Foundation/Foundation.h>

@protocol ServerCommunicatorDelegate <NSObject>
- (void)fetchingGroupsFailedWithError:(NSError *)error;
@optional
- (void)receivedCategoriesJSON:(NSData *)objectNotation;
- (void)receivedSearchJSON:(NSData *)objectNotation;
- (void)receivedRecomendationsSearchJSON:(NSData *)objectNotation;
- (void)receivedPlacesForCategoryJSON:(NSData *)objectNotation;
@end
