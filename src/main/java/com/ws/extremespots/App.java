package com.ws.extremespots;



import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

import java.io.PrintWriter;


public class App extends Object {

    public static void main (String args[]) {
        System.out.println( "Hello World!" );

        // create an empty graph
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource r = model.createResource();

        // add the property
        r.addProperty(RDFS.label, model.createLiteral("chat", "en"))
                .addProperty(RDFS.label, model.createLiteral("chat", "fr"))
                .addProperty(RDFS.label, model.createLiteral("<em>chat</em>", true));

        // write out the graph
        model.write(new PrintWriter(System.out));
        System.out.println();

        // create an empty graph
        model = ModelFactory.createDefaultModel();

        // create the resource
        r = model.createResource();

        // add the property
        r.addProperty(RDFS.label, "11")
                .addLiteral(RDFS.label, 11);

        // write out the graph
        model.write( System.out, "N-TRIPLE");
    }
}
