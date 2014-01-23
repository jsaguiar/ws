#import "MyLocation.h"

@implementation MyLocation
@synthesize name = _name;
@synthesize address = _address;
@synthesize coordinate = _coordinate;
@synthesize price = _price;
@synthesize rating = _rating;
@synthesize descrip = _descrip;
@synthesize recomend=_recomend;

- (id)initWithName:(NSString*)name address:(NSString*)address coordinate:(CLLocationCoordinate2D)coordinate price:(NSNumber*)price rating:(NSNumber*)rating descrip:(NSString*)descrip isRecommendation:(BOOL) recomend {
    if ((self = [super init])) {
        _name = [name copy];
        _address = [address copy];
        _coordinate = coordinate;
        _rating=rating;
        _descrip=descrip;
        _price=price;
        if(recomend)
            _recomend=[NSNumber numberWithInt:1];
        else{
            _recomend=[NSNumber numberWithInt:0];
        }
    }
    return self;
}

- (NSString *)title {
    if ([_name isKindOfClass:[NSNull class]]) 
        return @"Unknown charge";
    else
        return _name;
}

- (NSString *)subtitle {
    return _address;
}



@end