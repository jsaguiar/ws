package models.extremespots;


import models.semantic.MapPoint;
import models.semantic.Search;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.server.SPARQLServer;
import org.apache.jena.fuseki.server.ServerConfig;
*/

public class App extends Object {


    public static void main(String args[]) {
        // create an empty graph



        Path currentRelativePath = Paths.get("");
        String s2 = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s2);

        Search s = new Search();
        String query = "portuguese restaurant name is silva";
        ArrayList<MapPoint> arr = s.search(query);
        System.out.println(arr.get(0).toString());
        // System.out.println(s.getResults());

    }

}
