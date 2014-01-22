package models.extremespots;


import models.semantic.Search;

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
        s.search(query);
        // System.out.println(s.getResults());

    }

}
