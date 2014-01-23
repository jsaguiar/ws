//
//  DetailVIew.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 1/13/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import "DetailVIew.h"

@implementation DetailVIew

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    NSLog(@"detail view init");
    return self;
}
- (IBAction)swipe:(UISwipeGestureRecognizer *)sender {
    NSLog(@"swipe");
}

- (IBAction)asdnjkasd:(id)sender {
    NSLog(@"swipe");

}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    self.translucentAlpha = 1;
    self.translucentStyle = UIBarStyleBlack;
    self.translucentTintColor = [UIColor clearColor];
    self.backgroundColor = [UIColor clearColor];
}


@end
