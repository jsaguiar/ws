//
//  BrowseView.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/12/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h> 
#import "ILTranslucentView.h"
#import "PlaceCategory.h"


@interface BrowseView : ILTranslucentView <UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *browseTableView;
@property NSArray *info;

@end
