//
//  MyCostumCell.m
//  ws_project
//
//  Created by Joao Aguiar on 1/19/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import "MyCostumCell.h"

@implementation MyCostumCell
@synthesize label=_label;
@synthesize button=_button;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
