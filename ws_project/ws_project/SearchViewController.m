//
//  SearchViewController.m
//  ws_project
//
//  Created by Joao Aguiar on 1/22/14.
//  Copyright (c) 2014 Joao Aguiar. All rights reserved.
//

#import "SearchViewController.h"
#import "MyLocation.h"
@interface SearchViewController ()

@end

@implementation SearchViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    self.manager = [[ServerManager alloc] init];
    self.manager.communicator = [[ServerCommunicator alloc] init];
    self.manager.communicator.delegate = _manager;
    self.manager.delegate = self;
    self.detailViewController = (DetailViewController *)[[self.splitViewController.viewControllers lastObject] topViewController];
    
    
}
-(void)viewWillDisappear:(BOOL)animated{
    self.detailViewController.recommendateSpots=nil;
    self.detailViewController.detailItem=nil;
    self.receivedSearch=nil;
    self.searchBar.text=@"";

}
-(void)viewWillAppear:(BOOL)animated{
    self.detailViewController.isBrowsing=NO;
    [self.tableView reloadData];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.receivedSearch.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    Place *show=self.receivedSearch[indexPath.row];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"searchCell" forIndexPath:indexPath];
    cell.textLabel.text=show.name;
    return cell;
}

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [self.detailViewController.mapView selectAnnotation:[self.detailViewController.mkRecomendation objectAtIndex:indexPath.row]
      animated:YES];
    
    //[mapView selectAnnoation:foundAnnotation animated:YES];

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
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
 {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

#pragma -mark searchBar

-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [self.manager fetchSearch:searchBar.text];

    [searchBar resignFirstResponder];
    
}
-(void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar{
    self.detailViewController.recommendateSpots=nil;
    self.detailViewController.detailItem=nil;
    self.receivedSearch=nil;
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.tableView reloadData];
    });
}

-(BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar{
    return YES;
}
-(void)fetchingGroupsFailedWithError:(NSError *)error{
    NSLog(@"fetchingGroupsFailedWithError searchView");
}

-(void) didReceiveSearch:(NSArray *)groups{
    NSLog(@"received all seacrh");
    self.receivedSearch=groups;
    [self.manager fetchRecommendationSearch:self.searchBar.text];
}
-(void) didReceiveRecomendationSearch:(NSArray *)objectNotation{
    self.recommendationSpots=objectNotation;
    self.detailViewController.recommendateSpots=self.recommendationSpots;
    self.detailViewController.detailItem=self.receivedSearch;
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.tableView reloadData];
    });
    
    NSLog(@"Recommendation Search DONE");
}

@end
