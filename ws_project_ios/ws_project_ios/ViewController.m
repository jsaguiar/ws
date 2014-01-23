//
//  ViewController.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/9/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "ViewController.h"
#define METERS_PER_MILE 1609.344
#import "ILTranslucentView.h"
#import "ServerManager.h"
#import "ServerCommunicator.h"
#import "Place.h"
#import "MyLocation.h"
#import "MBProgressHUD.h"
#import "DetailVIew.h"

@implementation ViewController
@synthesize leftMenuHidded,searchMenuHidded;

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    [self setNeedsStatusBarAppearanceUpdate];
    
    
    self.showLeftMenu.backgroundColor = [UIColor blackColor];
    [self.showLeftMenu setImage:[UIImage imageNamed:@"glasses"] forState:UIControlStateSelected];
    [self.showLeftMenu setImage:[UIImage imageNamed:@"glasses"] forState:UIControlStateNormal];
    self.leftMenuHidded=TRUE;
    
    self.btnSearch.backgroundColor=[UIColor blackColor];
    [self.btnSearch setImage:[UIImage imageNamed:@"magnify.png"] forState:UIControlStateSelected];
    [self.btnSearch setImage:[UIImage imageNamed:@"magnify.png"] forState:UIControlStateNormal];
    self.searchMenuHidded=TRUE;
    
    
    CGRect aux= self.viewLeftMenu.frame;
    aux.origin.x= -self.viewLeftMenu.frame.size.width;
    [self.viewLeftMenu setFrame:aux];
    
    aux=self.showLeftMenu.frame;
    aux.origin.x= 0;
    [self.showLeftMenu setFrame:aux];
    
    
    aux= self.searchView.frame;
    aux.origin.x= -self.viewLeftMenu.frame.size.width;
    [self.searchView setFrame:aux];
    
    aux=self.btnSearch.frame;
    aux.origin.x= 0;
    [self.btnSearch setFrame:aux];
    
    
    
    UISwipeGestureRecognizer *swipeL = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(hideMenu)];
    swipeL.direction=UISwipeGestureRecognizerDirectionLeft;
    [self.viewLeftMenu addGestureRecognizer:swipeL];
    
    
    
    UISwipeGestureRecognizer *swipeLe = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(hideSearchView)];
    swipeLe.direction=UISwipeGestureRecognizerDirectionLeft;
    [self.searchView addGestureRecognizer:swipeLe];
    
    
    self.mapView.delegate= self;
    
    
    
    
    self.manager = [[ServerManager alloc] init];
    self.manager.communicator = [[ServerCommunicator alloc] init];
    self.manager.communicator.delegate = _manager;
    self.manager.delegate = self;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showPlacesLocation:) name:@"showLocation" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(removeAllLocations:) name:@"removeAllLocations" object:nil];

    
    [self.manager fetchCategories:@"Spot"];
    
    
}
- (void) removeAllLocations:(NSNotification*) notification{
    
    dispatch_async(dispatch_get_main_queue(), ^{
     [self removeAnnotations:self.mapView.annotations animated:NO];
     });
}

- (void) showPlacesLocation :(NSNotification*) notification{
    [MBProgressHUD showHUDAddedTo:self.viewLeftMenu animated:YES];
    //hud.labelText = @"Places localization..";
    [self.manager fetchPlacesForCategory:self.viewLeftMenu.categoriesChosen];
}

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


- (void) didReceivePlacesForCategory:(NSArray *)groups{
    
    NSLog(@"didReceivePlacesForCategory START");
    for (id<MKAnnotation> annotation in self.mapView.annotations) {
         dispatch_async(dispatch_get_main_queue(), ^{
             [_mapView removeAnnotation:annotation];
             });
        
        
    }

    NSMutableArray *places = [[NSMutableArray alloc] init];
    
    for (Place * row in groups) {
        CLLocationCoordinate2D coordinate;
        
        coordinate.latitude =  @([row.lat floatValue]).doubleValue;
        coordinate.longitude = @([row.lng floatValue]).doubleValue;
        MyLocation *annotation = [[MyLocation alloc] initWithName:row.name address:nil coordinate:coordinate] ;
        [places addObject:annotation];
        
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.mapView addAnnotations:places];
    });
    
    NSLog(@"didReceivePlacesForCategory END");
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD hideHUDForView:self.viewLeftMenu animated:YES];
    });
    
}

- (MKAnnotationView *)mapView:(MKMapView *)map viewForAnnotation:(id <MKAnnotation>)annotation
{
    
    MKPinAnnotationView *pin = (MKPinAnnotationView *) [self.mapView dequeueReusableAnnotationViewWithIdentifier: @"asdf"];
    
    if (pin == nil)
        pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier: @"asdf"];
    else
        pin.annotation = annotation;
    pin.userInteractionEnabled = YES;
    UIButton *disclosureButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    [disclosureButton setFrame:CGRectMake(0, 0, 30, 30)];
    
    pin.rightCalloutAccessoryView = disclosureButton;
    pin.pinColor = MKPinAnnotationColorRed;
    pin.animatesDrop = NO;
    [pin setEnabled:YES];
    [pin setCanShowCallout:YES];
    return pin;
}
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
    

    CGSize padding = (CGSize){ (self.view.frame.size.height-self.view.frame.size.width)/2 , 0 };
    self.detail.frame = (CGRect){padding.width, padding.height, self.detail.frame.size};
    
    
    
    //set properties of point of interest
    
    self.detail.lblName.text=annView.name;
    self.detail.imgViewPrice.hidden=YES;
    int price=4;
    int star=3;
    
    
    
    
    
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

    

    
    CGRect aux=   self.detail.frame;
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

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(MKAnnotationView *)sender
{
    if ([segue.identifier isEqualToString:@"DetailVC"])
    {
        //DetailViewController *destinationViewController = segue.destinationViewController;
        NSLog(@"NICE");
        // grab the annotation from the sender
        
        //destinationViewController.receivedLocation = sender.annotation;
    } else {
        NSLog(@"PFS:something else");
    }
}

-(NSArray*) categorieTree:(NSString*)categorie {
    NSMutableArray *array=[[NSMutableArray alloc]init];
    
    
    return array;
}


- (void)didReceiveCategories:(NSArray *)groups
{
    _groups = groups;
    self.categories=groups;
    NSLog(@"didReceiveGroups %d",self.categories.count);
    self.viewLeftMenu.info=groups;
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.viewLeftMenu.browseTableView reloadData];
    });
    
}
- (void)startFetchingGroups:(NSNotification *)notification
{
    //NSLog(@"startFetchingGroups\n");
    //[_manager fetchCategories];
}

- (void)fetchingGroupsFailedWithError:(NSError *)error
{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
    //[MBProgressHUD hideHUDForView:self.view animated:YES];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    // 1
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = -33.85694535899245;
    zoomLocation.longitude= 151.2150049209595;
    
    // 2
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 15*METERS_PER_MILE, 0.5*METERS_PER_MILE);
    
    // 3
    [_mapView setRegion:viewRegion animated:YES];
}


- (void)hideMenu
{
    if (leftMenuHidded){
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:1];
        
        CGRect aux= self.viewLeftMenu.frame;
        aux.origin.x= 0;
        [self.viewLeftMenu setFrame:aux];
        
        aux=self.showLeftMenu.frame;
        aux.origin.x= self.viewLeftMenu.frame.size.width;
        [self.showLeftMenu setFrame:aux];
        [UIView commitAnimations];
        leftMenuHidded=FALSE;        
    }
    else{
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:1];
        
        CGRect aux= self.viewLeftMenu.frame;
        aux.origin.x= -self.viewLeftMenu.frame.size.width;
        [self.viewLeftMenu setFrame:aux];
        
        aux=self.showLeftMenu.frame;
        aux.origin.x= 0;
        [self.showLeftMenu setFrame:aux];
        [UIView commitAnimations];
        leftMenuHidded=TRUE;
        
    }
    
    
}

- (void)hideSearchView
{
    if (searchMenuHidded){
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:1];
        
        CGRect aux= self.searchView.frame;
        aux.origin.x= 0;
        [self.searchView setFrame:aux];
        
        aux=self.btnSearch.frame;
        aux.origin.x= self.searchView.frame.size.width;
        [self.btnSearch setFrame:aux];
        
        [UIView commitAnimations];
        searchMenuHidded=FALSE;
    }
    else{
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:1];
        
        CGRect aux= self.searchView.frame;
        aux.origin.x= -self.searchView.frame.size.width;
        [self.searchView setFrame:aux];
        
        aux=self.btnSearch.frame;
        aux.origin.x= 0;
        [self.btnSearch setFrame:aux];
        [UIView commitAnimations];
        searchMenuHidded=TRUE;
        
    }
    
    
}


- (IBAction)clickBtnLeftMenu:(id)sender {
    [self hideMenu];
}

- (IBAction)clickBtnSearch:(id)sender {
    [self hideSearchView];
}

@end
