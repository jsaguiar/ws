//
//  ServerCommunicator.m
//  ws_project_ios
//
//  Created by Joao Aguiar on 12/14/13.
//  Copyright (c) 2013 Joao Aguiar. All rights reserved.
//

#import "ServerCommunicator.h"
#import "ServerCommunicatorDelegate.h"
#import "PlaceCategory.h"

#define URL @"http://192.168.1.3:3030"
#define PREFIX @"/data/query?query=PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>PREFIX owl:<http://www.w3.org/2002/07/owl#>PREFIX fn:<http://www.w3.org/2005/xpath-functions#>PREFIX apf:<http://jena.hpl.hp.com/ARQ/property#>PREFIX dc:<http://purl.org/dc/elements/1.1/>PREFIX project:<http://www.semanticweb.org/joao/ontologies/2013/10/POIs#> "


@implementation ServerCommunicator



- (void)getPlacesForCategory: (NSArray*) category{
    NSLog(@"getPlacesForCategory category= %@",category);
    PlaceCategory *aux=[category objectAtIndex:0];
    
    NSString *query=[NSString stringWithFormat:@"SELECT ?name ?id ?lat ?lng WHERE { { ?spot project:HasId  ?id . ?spot  project:HasName ?name. ?spot  project:HasLat ?lat. ?spot  project:HasLng ?lng. ?category rdfs:subClassOf* project:%@. ?spot rdf:type ?category. }",aux.name];
    
    
    NSString *query1;
    for (int i=1; i<category.count; i++) {
        aux=[category objectAtIndex:i];
        query1=[NSString stringWithFormat:@"UNION { ?spot project:HasId ?id. ?spot project:HasName ?name. ?spot project:HasLat ?lat. ?spot  project:HasLng ?lng. ?category rdfs:subClassOf* project:%@. ?spot rdf:type ?category.  }",aux.name];
        query=[NSString stringWithFormat: @"%@%@",query,query1];
        
    }
    
    NSLog(@"GET PLACES FOR CATEGORIES QUERY\n%@\n\n",query);
    
    NSString *urlAsString=[NSString stringWithFormat: @"%@%@%@}",URL, PREFIX, query];
    //urlAsString= [NSJSONSerialization dataWithJSONObject:urlAsString options:NSJSONReadingAllowFragments error:NULL];
    urlAsString=[urlAsString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingGroupsFailedWithError:error];
        } else {
            [self.delegate receivedPlacesForCategoryJSON:data];
        }
    }];

}

- (void)getCategories: (NSString*) category
{

//NSString *query=@"Select distinct ?category WHERE { ?category rdfs:subClassOf* project:Spot. }";
    NSString *query=[NSString stringWithFormat:@"Select ?category (SAMPLE(?label) as ?LABEL ) (COUNT(?category) AS ?instances_count ) WHERE { ?category rdfs:subClassOf project:%@. ?category rdfs:label ?label. ?category_sons rdfs:subClassOf* ?category. ?spot rdf:type ?category_sons. } GROUP BY ?category ORDER BY ?category",category];

    NSLog(@"GET GATEGORIES QUERY\n%@\n\n",query);
    
    NSString *urlAsString=[NSString stringWithFormat: @"%@%@%@",URL, PREFIX,query];
    urlAsString=[urlAsString stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];


    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingGroupsFailedWithError:error];
        } else {
            [self.delegate receivedCategoriesJSON:data];
        }
    }];
}

@end
