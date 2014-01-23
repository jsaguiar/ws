//
//  DetailViewController.m
//  ws_project
//
//  Created by Joao Aguiar on 1/19/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import "DetailViewController.h"
#define METERS_PER_MILE 1609.344

@interface DetailViewController ()

@property (strong, nonatomic) UIPopoverController *masterPopoverController;
- (void)configureView;
@end

@implementation DetailViewController
@synthesize leftMenuHidded,searchMenuHidded;

#pragma mark - Managing the detail item

- (void)setDetailItem:(id)newDetailItem
{
    if (_detailItem != newDetailItem) {
        _detailItem = newDetailItem;
        
        // Update the view.
        [self configureView];
    }
    
    if (self.masterPopoverController != nil) {
        [self.masterPopoverController dismissPopoverAnimated:YES];
    }
    
}

- (void)configureView
{
    if (self.mapView.annotations.count>0) {
        [self removeAnnotations:self.mapView.annotations animated:NO];
    }

    if (self.detailItem) {
        [self showPlacesLocation];
        
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    self.navigationController.navigationBarHidden =YES;
    
    
    self.showLeftMenu.backgroundColor = [UIColor blackColor];
    [self.showLeftMenu setImage:[UIImage imageNamed:@"glasses"] forState:UIControlStateSelected];
    [self.showLeftMenu setImage:[UIImage imageNamed:@"glasses"] forState:UIControlStateNormal];
    self.leftMenuHidded=TRUE;
    
    self.btnSearch.backgroundColor=[UIColor blackColor];
    [self.btnSearch setImage:[UIImage imageNamed:@"magnify.png"] forState:UIControlStateSelected];
    [self.btnSearch setImage:[UIImage imageNamed:@"magnify.png"] forState:UIControlStateNormal];
    self.searchMenuHidded=TRUE;
    
    [self configureView];
    
    
    self.manager = [[ServerManager alloc] init];
    self.manager.communicator = [[ServerCommunicator alloc] init];
    self.manager.communicator.delegate = _manager;
    self.manager.delegate = self;
    
}
- (void)viewWillAppear:(BOOL)animated {
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = -33.85694535899245;
    zoomLocation.longitude= 151.2150049209595;
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 15*METERS_PER_MILE, 0.5*METERS_PER_MILE);
    [_mapView setRegion:viewRegion animated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Split view

- (void)splitViewController:(UISplitViewController *)splitController willHideViewController:(UIViewController *)viewController withBarButtonItem:(UIBarButtonItem *)barButtonItem forPopoverController:(UIPopoverController *)popoverController
{
    barButtonItem.title = NSLocalizedString(@"Master", @"Master");
    [self.navigationItem setLeftBarButtonItem:barButtonItem animated:YES];
    self.masterPopoverController = popoverController;
}

- (void)splitViewController:(UISplitViewController *)splitController willShowViewController:(UIViewController *)viewController invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem
{
    // Called when the view is shown again in the split view, invalidating the button and popover controller.
    [self.navigationItem setLeftBarButtonItem:nil animated:YES];
    self.masterPopoverController = nil;
}
- (BOOL)splitViewController:(UISplitViewController *)svc shouldHideViewController:
(UIViewController *)vc inOrientation:(UIInterfaceOrientation)orientation
{
    return NO;
}

#pragma mark - MAP
- (void)removeAnnotations:(NSArray *)annotations animated:(BOOL)shouldAnimate {
    [self.mapView removeAnnotations:annotations];
    NSLog(@"removeAnnotations");
    /*if (!shouldAnimate)
     [self.mapView removeAnnotations:annotations];
     else {
     NSTimeInterval delay = 0.0;
     for (id<MKAnnotation> annotation in annotations) {
     MKAnnotationView *annotationView = [self.mapView viewForAnnotation:annotation];
     CGRect endFrame = annotationView.frame;
     endFrame = CGRectMake(
     annotationView.frame.origin.x,
     annotationView.frame.origin.y - self.mapView.bounds.size.height,
     annotationView.frame.size.width,
     annotationView.frame.size.height);
     [UIView animateWithDuration:0.2
     delay:delay
     options:UIViewAnimationOptionAllowUserInteraction
     animations:^{
     annotationView.frame = endFrame;
     }
     completion:^(BOOL finished) {
     [self.mapView removeAnnotation:annotation];
     }];
     delay += 0.000;
     }
     }*/
}



- (MKAnnotationView *)mapView:(MKMapView *)map viewForAnnotation:(id <MKAnnotation>)annotation
{
    MyLocation *help=annotation;
    MKPinAnnotationView *pin = (MKPinAnnotationView *) [self.mapView dequeueReusableAnnotationViewWithIdentifier: @"asdf"];
    
    if (pin == nil)
        pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier: @"asdf"];
    else
        pin.annotation = annotation;
    pin.userInteractionEnabled = YES;
    UIButton *disclosureButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    [disclosureButton setFrame:CGRectMake(0, 0, 30, 30)];
    
    pin.rightCalloutAccessoryView = disclosureButton;
    if([help.recomend intValue]==0){
    pin.pinColor = MKPinAnnotationColorRed;
    }
    else{
        pin.pinColor=MKPinAnnotationColorGreen;
    }
    pin.animatesDrop = NO;
    [pin setEnabled:YES];
    [pin setCanShowCallout:YES];
    return pin;
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
    if (![view.annotation isKindOfClass:[MyLocation class]])
        return;
    
    NSArray *nibContents = [[NSBundle mainBundle] loadNibNamed:@"DetailView" owner:nil options:nil];
    
    MyLocation *annView = (MyLocation *)view.annotation;
    self.detail = [nibContents lastObject];
    
    
    
    self.detail.userInteractionEnabled = YES;
    UISwipeGestureRecognizer *swipeD = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeDown:)];
    swipeD.direction=UISwipeGestureRecognizerDirectionDown;
    [self.detail addGestureRecognizer:swipeD];
    
    
    UISwipeGestureRecognizer *swipeU = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeUp:)];
    swipeU.direction=UISwipeGestureRecognizerDirectionUp;
    [self.detail addGestureRecognizer:swipeU];
    
    UISwipeGestureRecognizer *swipeL = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeLeft:)];
    swipeL.direction=UISwipeGestureRecognizerDirectionLeft;
    [self.detail addGestureRecognizer:swipeL];
    
    
    UISwipeGestureRecognizer *swipeR = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeRight:)];
    swipeR.direction=UISwipeGestureRecognizerDirectionRight;
    [self.detail addGestureRecognizer:swipeR];
    
    
    CGSize padding = (CGSize){ 0 , 0 };
    self.detail.frame = (CGRect){padding.width, padding.height, self.detail.frame.size};
    
    
    
    //set properties of point of interest
    
    self.detail.lblName.text=annView.name;
    self.detail.lblAddress.text=annView.address;
    self.detail.txtComment.text=annView.descrip;
    
    [self.detail.txtComment setFont:[UIFont systemFontOfSize:17]];
    [self.detail.txtComment setTextColor:[UIColor whiteColor]];

    
    
    self.detail.imgViewPrice.hidden=YES;
    int price=[annView.price intValue];
    int star=[annView.rating intValue]/2;
    
    
    for (int i=0; i<5; i++) {
        UIImageView *iv=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"pricetag_b"] highlightedImage:[UIImage imageNamed:@"pricetag"] ];
        CGRect aux=self.detail.imgViewPrice.frame;
        aux.origin.x+=30*i;
        iv.frame=aux;
        if(i<price){
            iv.highlighted=YES;
        }
        [self.detail addSubview:iv];
    }
    
    self.detail.imgViewStart.hidden=YES;
    
    for (int i=0; i<5; i++) {
        UIImageView *iv=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"star_b"] highlightedImage:[UIImage imageNamed:@"star"] ];
        CGRect aux=self.detail.imgViewStart.frame;
        aux.origin.x+=30*i;
        iv.frame=aux;
        if(i<star){
            iv.highlighted=YES;
        }
        [self.detail addSubview:iv];
    }
    
    
    
    
    CGRect aux= self.detail.frame;
    aux.origin.y=+self.detail.frame.size.height;
    self.detail.frame=aux;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    aux= self.detail.frame;
    aux.origin.y=0;
    self.detail.frame=aux;
    [ self.detail setFrame:aux];
    [UIView commitAnimations];
    
    
    [self.view addSubview:self.detail];
    
}


#pragma mark - Request
- (void) showPlacesLocation {
    //hud.labelText = @"Places localization..";
    NSArray *aux = self.detailItem;
    if  (aux.count>0 && self.isBrowsing){
         [self.manager fetchPlacesForCategory:self.detailItem];
    }
    if (!self.isBrowsing) {
        self.mkRecomendation=[[NSMutableArray alloc]init];
        if (self.recommendateSpots.count>0) {
            [self putPlacesForArray:self.recommendateSpots withBluePin:YES];

        }
        if (((NSArray*)self.detailItem).count>0) {
            [self putPlacesForArray:self.detailItem withBluePin:NO] ;
        }
    }
}
-(void) putPlacesForArray:(NSArray*) groups withBluePin:(BOOL)bluePin{
    for (id<MKAnnotation> annotation in self.mapView.annotations) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [_mapView removeAnnotation:annotation];
        });
        
        
    }
    
    NSMutableArray *places = [[NSMutableArray alloc] init];
    int i=0;
    for (Place * row in groups) {
        i++;
        CLLocationCoordinate2D coordinate;
        coordinate.latitude =  @([row.lat floatValue]).doubleValue;
        coordinate.longitude = @([row.lng floatValue]).doubleValue;
        
        //if(i>20 && bluePin) continue;
        MyLocation *annotation =[[MyLocation alloc] initWithName:row.name address:row.address coordinate:coordinate price:row.price rating:row.rating descrip:row.descrip isRecommendation:bluePin];
        [places addObject:annotation];
        
        
    }
    self.mkRecomendation=places;
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapView addAnnotations:places];
    });

}

- (void) didReceivePlacesForCategory:(NSArray *)groups{
    
    NSLog(@"didReceivePlacesForCategory START");
    [self putPlacesForArray:groups withBluePin:NO];
    NSLog(@"didReceivePlacesForCategory END");
    /* dispatch_async(dispatch_get_main_queue(), ^{
     [MBProgressHUD hideHUDForView:self.viewLeftMenu animated:YES];
     });*/
    
}


#pragma mark - Gestures
-(void) dismissDetail{
    [self.detail removeFromSuperview];
    
}

- (void)handleSwipeDown:(UISwipeGestureRecognizer *)swipeRecognizer{
    CGRect aux=   self.detail.frame;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    aux.origin.y=self.detail.frame.size.height;
    self.detail.frame=aux;
    [ self.detail setFrame:aux];
    [UIView commitAnimations];
    
    [UIView setAnimationDidStopSelector:@selector(dismissDetail)];
    [UIView setAnimationDelegate:self];
    
}

- (void)handleSwipeUp:(UISwipeGestureRecognizer *)swipeRecognizer{
    CGRect aux=   self.detail.frame;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    aux.origin.y= -aux.size.height;
    self.detail.frame=aux;
    [ self.detail setFrame:aux];
    [UIView commitAnimations];
    
    [UIView setAnimationDidStopSelector:@selector(dismissDetail)];
    [UIView setAnimationDelegate:self];
}



- (void)handleSwipeLeft:(UISwipeGestureRecognizer *)swipeRecognizer{
    CGRect aux=   self.detail.frame;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    aux.origin.x=-aux.size.width;
    self.detail.frame=aux;
    [ self.detail setFrame:aux];
    [UIView commitAnimations];
    
    [UIView setAnimationDidStopSelector:@selector(dismissDetail)];
    [UIView setAnimationDelegate:self];
    
}
- (void)handleSwipeRight:(UISwipeGestureRecognizer *)swipeRecognizer{
    CGRect aux=   self.detail.frame;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    aux.origin.x=self.view.bounds.size.width+aux.size.width;
    self.detail.frame=aux;
    [ self.detail setFrame:aux];
    [UIView commitAnimations];
    
    
    [UIView setAnimationDidStopSelector:@selector(dismissDetail)];
    [UIView setAnimationDelegate:self];
    
}

- (void)fetchingGroupsFailedWithError:(NSError *)error{
    NSLog(@"fetching Groups Failed DetailVIew");
}



@end
