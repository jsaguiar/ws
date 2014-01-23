//
//  BrowseView.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/12/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "BrowseView.h"

@implementation BrowseView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    
    return self;
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    self.translucentAlpha = 1;
    self.translucentStyle = UIBarStyleBlack;
    self.translucentTintColor = [UIColor clearColor];
    self.backgroundColor = [UIColor clearColor];
    self.browseTableView.backgroundColor=[UIColor clearColor];
    
    
    self.refreshControl = [[UIRefreshControl alloc] init];
    self.refreshControl.tintColor=[UIColor whiteColor];
    [self.refreshControl addTarget:self action:@selector(refresh) forControlEvents:UIControlEventValueChanged];
    
    [self.browseTableView addSubview:self.refreshControl];
    self.categoriesChosen  =[[NSMutableArray alloc]init];
    
    self.selected = [[NSMutableIndexSet alloc] init];
    self.browseTableView.delegate=self;
    self.browseTableView.dataSource=self;

    
    // Drawing code
}
-(void) refresh{
    [self.refreshControl endRefreshing];

}


-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *tempView=[[UIView alloc]initWithFrame:CGRectMake(0,0,300,44)];
    tempView.backgroundColor=[UIColor clearColor];
    UILabel *tempLabel=[[UILabel alloc]initWithFrame:CGRectMake(0,0,300,44)];
    tempLabel.backgroundColor=[UIColor clearColor];
    tempLabel.text=@"CATEGORIES";
    tempLabel.textColor=[UIColor whiteColor];
    [tempView addSubview: tempLabel];
    return tempView;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 50;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.info.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    PlaceCategory *category= self.info[indexPath.row];
    cell.textLabel.text= category.label;
    cell.backgroundColor=[UIColor clearColor];
 
    if ([self.selected containsIndex:indexPath.row]) {
        cell.backgroundColor=[UIColor lightGrayColor];
    }
    
    if ([category.childNum intValue] >0){
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    }
    else{
        [cell setAccessoryType:UITableViewCellAccessoryNone];

    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    PlaceCategory *category= self.info[indexPath.row];

    UITableViewCell *cell = [self.browseTableView cellForRowAtIndexPath:indexPath];
    if (cell.backgroundColor == [UIColor lightGrayColor]){
        [self.selected removeIndex:indexPath.row];
        for (PlaceCategory* row in self.categoriesChosen) {
            if (row.name==category.name) {
                [self.categoriesChosen removeObject:row];
                break;
            }
        }

    }
    else{
        [self.selected addIndex:indexPath.row];
        [self.categoriesChosen addObject:category];

    }
    
    
    [self.browseTableView reloadData];
   
    //self.oneCategory =category.name;
    //NSLog(@"oneCategory %@",self.oneCategory);
    if(self.categoriesChosen.count>0)
        [[NSNotificationCenter defaultCenter] postNotificationName:@"showLocation" object:nil];
    else{
        [[NSNotificationCenter defaultCenter] postNotificationName:@"removeAllLocations" object:nil];

      
    }


}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
 }
 else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

/*
 #pragma mark - Navigation
 
 // In a story board-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
 {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 
 */


@end
