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

@interface ViewController : UIViewController
@property (strong, nonatomic) IBOutlet MKMapView *mapView;
@property (weak, nonatomic) IBOutlet UIButton *showLeftMenu;

@property (weak, nonatomic) IBOutlet BrowseView *viewLeftMenu;
@property (weak, nonatomic) IBOutlet UITableView *browseTableView;
@property BOOL leftMenuHidded;

@property NSArray *categories;

- (IBAction)clickBtnLeftMenu:(id)sender;


@end
