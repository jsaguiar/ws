//
//  ViewController.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/9/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "BrowseView.h"
#import "PlaceCategory.h"
#import "ServerManager.h"
#import "SearchView.h"
#import "DetailVIew.h"

@interface ViewController : UIViewController <ServerManagerDelegate,MKMapViewDelegate, UIGestureRecognizerDelegate> {
    NSArray *_groups;
}

@property (strong, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIButton *showLeftMenu;
@property (weak, nonatomic) IBOutlet BrowseView *viewLeftMenu;
@property (weak, nonatomic) IBOutlet SearchView *searchView;
@property BOOL leftMenuHidded;
@property BOOL searchMenuHidded;
@property (weak, nonatomic) IBOutlet UIButton *btnSearch;

@property (weak,nonatomic) DetailVIew *detail;

@property NSArray *categories;
@property ServerManager *manager;
- (IBAction)clickBtnLeftMenu:(id)sender;

- (IBAction)clickBtnSearch:(id)sender;
@end
