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
@interface ViewController ()

@end

@implementation ViewController
@synthesize leftMenuHidded;

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    [self setNeedsStatusBarAppearanceUpdate];
    self.showLeftMenu.backgroundColor = [UIColor blackColor];
    [self.showLeftMenu setImage:[UIImage imageNamed:@"left_button"] forState:UIControlStateSelected];
    [self.showLeftMenu setImage:[UIImage imageNamed:@"right_button"] forState:UIControlStateNormal];
    self.leftMenuHidded=TRUE;
    
    
  
    CGRect aux= self.viewLeftMenu.frame;
    aux.origin.x= -self.viewLeftMenu.frame.size.width;
    [self.viewLeftMenu setFrame:aux];
    
    aux=self.showLeftMenu.frame;
    aux.origin.x= 0;
    [self.showLeftMenu setFrame:aux];
    
    self.categories = [NSArray arrayWithObjects:
                  [PlaceCategory PlaceCategoryWihtName:@"chocolate" withIdentifier:@"chocolate bar"],
                  [PlaceCategory PlaceCategoryWihtName:@"chocolate" withIdentifier:@"chocolate chip"],
                  [PlaceCategory PlaceCategoryWihtName:@"chocolate" withIdentifier:@"dark chocolate"],
                  [PlaceCategory PlaceCategoryWihtName:@"hard" withIdentifier:@"lollipop"],
                  [PlaceCategory PlaceCategoryWihtName:@"hard" withIdentifier:@"candy cane"],
                  [PlaceCategory PlaceCategoryWihtName:@"hard" withIdentifier:@"jaw breaker"],
                  [PlaceCategory PlaceCategoryWihtName:@"other" withIdentifier:@"caramel"],
                  [PlaceCategory PlaceCategoryWihtName:@"other" withIdentifier:@"sour chew"],
                  [PlaceCategory PlaceCategoryWihtName:@"other" withIdentifier:@"peanut butter cup"],
                  [PlaceCategory PlaceCategoryWihtName:@"other" withIdentifier:@"gummi bear"], nil];
    
    self.viewLeftMenu.info=self.categories;
    
    
    
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
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 0.5*METERS_PER_MILE, 0.5*METERS_PER_MILE);
    
    // 3
    [_mapView setRegion:viewRegion animated:YES];
}


- (void)hideMenu
{
    if (leftMenuHidded){
        [self.showLeftMenu setSelected:TRUE];
        
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
        [self.showLeftMenu setSelected:FALSE];
    
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



- (IBAction)clickBtnLeftMenu:(id)sender {
    [self hideMenu];
}

@end
