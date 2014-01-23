//
//  DetailVIew.h
//  ws_project_ios
//
//  Created by Joao Aguiar on 1/13/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ILTranslucentView.h"

@interface DetailVIew : ILTranslucentView
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblContact;
@property (weak, nonatomic) IBOutlet UILabel *lblAddress;
@property (weak, nonatomic) IBOutlet UITextView *txtComment;

@property (weak, nonatomic) IBOutlet UIImageView *imgViewPrice;
@property (weak, nonatomic) IBOutlet UIImageView *imgViewStart;


@end
