//
//  SearchView.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 1/12/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//
#import <UIKit/UIKit.h>
#import "ILTranslucentView.h"
#import "PlaceCategory.h"
#import "JSONModelLib.h"

@interface SearchView : ILTranslucentView <UITableViewDelegate, UITableViewDataSource,UISearchBarDelegate,UISearchDisplayDelegate>
@property (weak, nonatomic) IBOutlet UITableView *browseTableView;
@property (strong, nonatomic) UIRefreshControl *refreshControl;

@property (weak, nonatomic) IBOutlet UISearchBar *simpleSearch;

@property NSArray *info;

@end
