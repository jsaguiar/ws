#import "ServerManager.h"
#import "GroupBuilder.h"
#import "ServerCommunicator.h"

@implementation ServerManager

- (void)fetchCategories: (NSString*) categorie

{
    [self.communicator getCategories:categorie];
}

- (void)fetchPlacesForCategory: (NSArray*) categorie{
    [self.communicator getPlacesForCategory:categorie];
}

#pragma mark - MeetupCommunicatorDelegate

- (void)receivedCategoriesJSON:(NSData *)objectNotation
{
    NSError *error = nil;
    NSArray *groups = [GroupBuilder categoriesFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingGroupsFailedWithError:error];
        
    } else {
        [self.delegate didReceiveCategories:groups];
    }
}

-(void) receivedPlacesForCategoryJSON:(NSData *)objectNotation
{
    NSLog(@"receivedPlacesForCategoryJSON");
    NSError *error = nil;
    NSArray *groups = [GroupBuilder placesFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingGroupsFailedWithError:error];
        
    } else {
        [self.delegate didReceivePlacesForCategory:groups];
    }
}


- (void)receivedGroupsJSON:(NSData *)objectNotation
{
    NSError *error = nil;
    NSArray *groups = [GroupBuilder categoriesFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingGroupsFailedWithError:error];
        
    } else {
        [self.delegate didReceiveCategories:groups];
    }
}


- (void)fetchingGroupsFailedWithError:(NSError *)error
{
    [self.delegate fetchingGroupsFailedWithError:error];
}

@end