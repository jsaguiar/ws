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


        search();

        // System.out.println(s.getResults());

    }

    private static void search(){
        Search s = new Search();
        String query = "hotel";
        Search.search(query);
        if (s.vsON == false) {
            System.out.println("******************");
              System.out.println(Search.finalResults);
//            System.out.println(Search.recResults);
//            System.out.println(Search.recKeyword);
            System.out.println("******************");

        }else {
            System.out.println("A pesquisa é ambígua ou não foram encontrados produtos correspondentes.");
        }
    }
}
