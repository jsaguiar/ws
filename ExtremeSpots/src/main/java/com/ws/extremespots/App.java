package com.ws.extremespots;


import com.ws.semantic.Search;

import java.util.ArrayList;

/*
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.server.SPARQLServer;
import org.apache.jena.fuseki.server.ServerConfig;
*/

public class App extends Object {


    public static void main(String args[]) {
        // create an empty graph




        Search s = new Search();
        String query = "portuguese restaurant name is silva";
        System.out.println(s.search(query));
        // System.out.println(s.getResults());

    }

}
