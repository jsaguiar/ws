package com.ws.data;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ws.extremespots.StaticVariables;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 01/12/13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class Parser {
    Model ontModel;
    ArrayList<PointOfInterest> points_of_interest;
    String namespace;


    public Parser(){
        points_of_interest = new JsonParser().points_of_interest;
        ontModel = ManageOntology.importOntology();
        namespace = StaticVariables.ontology_name_space;
        Dataset ds = TDBFactory.createDataset(StaticVariables.triple_store_directory) ;
        Model tdbModel = ds.getDefaultModel() ;


        for(PointOfInterest poi: points_of_interest){
            addPointOfInterest(poi);
        }
        tdbModel.add(ontModel);
        tdbModel.commit();
        tdbModel.close();
    }

    public static String refactor_spot_category(String aux){
        aux=aux.replace(" / ","/");
        aux=aux.replace(" & ","/");
        aux=aux.replace("&","/");
        aux=aux.replace(" ","_");
        return aux;
    }
    private void addPointOfInterest(PointOfInterest poi){
        String category = refactor_spot_category(poi.categories.get(0).getName());
        String category_id = poi.categories.get(0).getId();
        Resource spot = ontModel.createResource(namespace+poi._id)
                .addProperty(RDF.type, category);


        String name = (poi.name!=null)? poi.name :"";
        spot.addProperty(ontModel.getProperty(namespace+"HasName"), name);

        String description = (poi.description!=null)? poi.description :"";
        spot.addProperty(ontModel.getProperty(namespace+"HasDescription"), description);

        String contact = (poi.contact.phone!=null)? poi.contact.phone :"";
        spot.addProperty(ontModel.getProperty(namespace+"HasContact"), contact);

        String id = (poi._id!=null)? poi._id :"";
        spot.addProperty(ontModel.getProperty(namespace+"HasId"), id);

        if(poi.location.lat!=null){
            spot.addProperty(ontModel.getProperty(namespace+"HasLat"), poi.location.lat.toString());
        }
        if(poi.location.lng!=null){
            spot.addProperty(ontModel.getProperty(namespace+"HasLng"), poi.location.lng.toString());
        }

        Resource description2 = ontModel.getProperty(namespace+"Description"+poi.description);
        if(description2 == null){
            description2 = ontModel.createResource(namespace+"Description"+poi.description);
            description2.addProperty(RDF.type, "Description");
        }
        description2.addProperty(ontModel.getProperty(namespace+"hasTargetSpot"), spot.getURI());




    }
    public static void main (String args[]) {
        try {
            FileUtils.deleteDirectory(new File(StaticVariables.triple_store_directory));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Parser parser = new Parser();

        FileOutputStream fop = null;
        File file;
        file = new File("current_ontology.xml");
        try {
            fop = new FileOutputStream(file);
            parser.ontModel.write(fop);
            fop.flush();
            fop.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        /*
        String s = "SELECT DISTINCT ?s { ?s ?p ?o }";

        //String s = "SELECT * WHERE { ?x < " + parser.ontModel.getProperty(parser.namespace +"HasId") + "> \" "+ "4e37f028aeb7c8fe3175fb6c"+ "\" .";

        // Parse
        Query query = QueryFactory.create(s) ;
        System.out.println(query) ;

        // Generate algebra
        Op op = Algebra.compile(query) ;
        op = Algebra.optimize(op) ;
        System.out.println(op) ;

        // Execute it.
        QueryIterator qIter = Algebra.exec(op, ManageOntology.importOntology()) ;

        // Results
        System.out.println("---------------------------------------------------------------------------");
        for ( ; qIter.hasNext() ; )
        {
            Binding b = qIter.nextBinding() ;
            System.out.println(b) ;
        }
        qIter.close() ;
               */
    /*
        System.out.println("########################################################################");

        StmtIterator iter = ManageOntology.importOntology().listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();         // get next statement
            Resource  subject   = stmt.getSubject();   // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode   object    = stmt.getObject();    // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");


            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }
    */

    }


}
