//
//  SearchViewController.h
//  ws_project
//
//  Created by Joao Aguiar on 1/22/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ServerManager.h"
#import "ServerCommunicator.h"
#import "DetailViewController.h"

@interface SearchViewController : UIViewController <UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate,UISearchDisplayDelegate,ServerManagerDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property ServerManager *manager;
@property (strong, nonatomic) DetailViewController *detailViewController;
@property (strong,nonatomic) NSArray *receivedSearch;
@property (strong,nonatomic) NSArray *recommendationSpots;
@end
