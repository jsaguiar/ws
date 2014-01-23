//
//  MasterViewController.m
//  ws_project
//
//  Created by Joao Aguiar on 1/19/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import "MasterViewController.h"

#import "DetailViewController.h"

@implementation MasterViewController

- (void)awakeFromNib
{
    self.clearsSelectionOnViewWillAppear = NO;
    self.preferredContentSize = CGSizeMake(320.0, 600.0);
    [super awakeFromNib];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    self.detailViewController = (DetailViewController *)[[self.splitViewController.viewControllers lastObject] topViewController];
    
    self.manager = [[ServerManager alloc] init];
    self.manager.communicator = [[ServerCommunicator alloc] init];
    self.manager.communicator.delegate = _manager;
    self.manager.delegate = self;
    self.selected=[[NSMutableIndexSet alloc]init];
    self.categoriesChosen=[[NSMutableArray alloc]init];
    
}

-(void) clearAll{
    self.detailViewController.detailItem=nil;
    self.selected=[[NSMutableIndexSet alloc]init];
    self.categoriesChosen=[[NSMutableArray alloc]init];
}
- (void) viewWillDisappear:(BOOL)animated{
    [self clearAll];
}
-(void)viewWillAppear:(BOOL)animated{
    self.detailViewController.isBrowsing=YES;
    if (self.category) {
        [self.manager fetchCategories:self.category];
        
    }else{
        [self.manager fetchCategories:@"Spot"];
    }
}


- (void)didReceiveCategories:(NSArray *)groups
{
    _groups = groups;
    self.categories=groups;
    NSLog(@"didReceiveGroups %d",self.categories.count);
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.tableView reloadData];
    });
    
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.categories.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"MyCostumCell";
    MyCostumCell *cell = (MyCostumCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"MyCostumCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
    }
    
    PlaceCategory *category= self.categories[indexPath.row];
    cell.backgroundColor=[UIColor clearColor];
    
    cell.label.text= category.label;
    [cell.button addTarget:self action:@selector(customActionPressed:) forControlEvents:UIControlEventTouchDown];
    
    [cell.button setHighlighted:NO];
    
    
    if ([self.selected containsIndex:indexPath.row]) {
        [cell.button setHighlighted:YES];
        cell.backgroundColor=[UIColor lightGrayColor];
    }
    
    if ([category.childNum intValue] >1){
        NSLog(@"%@-numclild %d",category.label,[category.childNum intValue]);
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    }
    else{
        [cell setAccessoryType:UITableViewCellAccessoryNone];
        
    }
    
    [cell bringSubviewToFront:cell.button];
    
    return cell;
}

-(void) selectRowAtIndexPath:(NSIndexPath*) indexPath{
    
    UITableViewCell *cell= [self.tableView cellForRowAtIndexPath:indexPath];

    PlaceCategory *category= self.categories[indexPath.row];
    
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
    
    NSArray *pass=[[NSArray alloc]initWithArray:self.categoriesChosen];
    self.detailViewController.detailItem = pass;
    
    [self.tableView reloadData];

}
-(void) customActionPressed:(UIButton *) sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tableView];
    NSIndexPath *indexPath = [self.tableView indexPathForRowAtPoint:rootViewPoint];
    [self selectRowAtIndexPath:indexPath];
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    PlaceCategory *category= self.categories[indexPath.row];
    if ([category.childNum intValue] >1){
        
        MasterViewController *abo = [[MasterViewController alloc] initWithNibName:nil bundle:nil];
        abo.category=category.name;
        abo.title=category.label;
        
        [self.navigationController pushViewController:abo animated:YES];
        
        [self clearAll];
    }
    else{
        [self selectRowAtIndexPath:indexPath];
    }
    
    
    
}


/*
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 [_objects removeObjectAtIndex:indexPath.row];
 [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
 } else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
 }
 }
 
 
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
- (void)fetchingGroupsFailedWithError:(NSError *)error{
    NSLog(@"ERROR fetching groups");
}

- (IBAction)btnClear:(id)sender {
    [self clearAll];
    [self.tableView reloadData];
}


@end
