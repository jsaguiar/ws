#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface MyLocation : NSObject <MKAnnotation> {
    NSString *_name;
    NSString *_address;
    NSString *_contact;
    NSNumber *_price;
    NSNumber *_rating;
    NSString *_descrip;
    NSNumber *_recomend;

    CLLocationCoordinate2D _coordinate;
}

@property (copy) NSString *name;
@property (copy) NSString *address;
@property (copy) NSString *contact;
@property (copy) NSNumber *price;
@property (copy) NSNumber *rating;
@property (copy) NSString *descrip;
@property (copy) NSNumber *recomend;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

- (id)initWithName:(NSString*)name address:(NSString*)address coordinate:(CLLocationCoordinate2D)coordinate price:(NSNumber*)price rating:(NSNumber*)rating descrip:(NSString*)descrip isRecommendation:(BOOL) recomend;

@end
