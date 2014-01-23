//
//  MasterViewController.h
//  ws_project
//
//  Created by Joao Aguiar on 1/19/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerManager.h"
#import "ServerCommunicator.h"
#import "PlaceCategory.h"
#import "MyCostumCell.h"


@class DetailViewController;

@interface MasterViewController : UITableViewController <ServerManagerDelegate>{
    NSArray *_groups;
}


@property (strong, nonatomic) DetailViewController *detailViewController;
@property ServerManager *manager;
@property NSArray *categories;
@property NSMutableIndexSet *selected;
@property  (strong,nonatomic) NSMutableArray *categoriesChosen;
@property (strong,nonatomic) NSString *category;
@end
