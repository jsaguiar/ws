//
//  DetailViewController.h
//  ws_project
//
//  Created by Joao Aguiar on 1/19/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerManager.h"
#import <MapKit/MapKit.h>
#import "Place.h"
#import "MyLocation.h"
#import "PlaceCategory.h"
#import "ServerCommunicator.h"
#import "DetailVIew.h"

@interface DetailViewController : UIViewController <UISplitViewControllerDelegate,MKMapViewDelegate,ServerManagerDelegate> {
    NSArray *_groups;
}


@property (strong, nonatomic) id detailItem;

@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;

@property (strong, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIButton *showLeftMenu;
@property (weak, nonatomic) IBOutlet UIButton *btnSearch;
@property BOOL leftMenuHidded;
@property BOOL searchMenuHidded;
@property ServerManager *manager;
@property (weak,nonatomic) DetailVIew *detail;
@property BOOL isBrowsing;
@property NSArray *recommendateSpots;
@property NSMutableArray *mkRecomendation;
@end
