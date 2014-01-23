#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

#import "ServerManagerDelegate.h"
#import "ServerCommunicatorDelegate.h"

@class ServerCommunicator;

@interface ServerManager : NSObject<ServerCommunicatorDelegate>

@property (strong, nonatomic) ServerCommunicator *communicator;
@property (weak, nonatomic) id<ServerManagerDelegate> delegate;

- (void)fetchCategories: (NSString*) category;
- (void)fetchPlacesForCategory: (NSArray*) categorie;
- (void)fetchSearch: (NSString*) search;
- (void)fetchRecommendationSearch: (NSString*) search;


@end
