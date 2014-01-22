package com.ws.data;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.ws.extremespots.StaticVariables;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

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
        aux=aux.replace(" / ",".");
        aux=aux.replace("/",".");

        aux=aux.replace(" & ","..");
        aux=aux.replace("&","..");

        aux=aux.replace(" ( ","LL.");
        aux=aux.replace("(","LL.");

        aux=aux.replace(" ) ",".LR");
        aux=aux.replace(")",".LR");

        aux=aux.replace(" ’ ",".....");
        aux=aux.replace("'",".....");

        aux=aux.replace(" ","_");


        return aux;
    }

    //address

    //price
    //rating
    //

    private void addPointOfInterest(PointOfInterest poi){
        String category =  (poi.categories.get(0).getName());
        String category_id = poi.categories.get(0).getId();
        Resource spot = ontModel.createResource(namespace+poi._id)
                .addProperty(RDF.type, ontModel.getProperty(namespace+refactor_spot_category(category)));



        if(poi.price!=null){
            spot.addProperty(ontModel.getProperty(namespace+"HasPrice"), (String)poi.price.get("tier"));
        }else{
            spot.addProperty(ontModel.getProperty(namespace+"HasPrice"), "-1");
        }
        if(poi.rating!=null){
            spot.addProperty(ontModel.getProperty(namespace+"HasRating"), ""+poi.rating);
        }else{
            spot.addProperty(ontModel.getProperty(namespace+"HasRating"), "-1");
        }
        if(poi.name!=null){
            spot.addProperty(ontModel.getProperty(namespace+"HasName"), poi.name);
        }

        if(!poi.location.getLocation().isEmpty()){
            spot.addProperty(ontModel.getProperty(namespace+"HasAddress"), poi.location.getLocation());
        }
        if(poi.contact.phone!=null)
            spot.addProperty(ontModel.getProperty(namespace+"HasContact"), poi.contact.phone);

        if(poi._id!=null)
            spot.addProperty(ontModel.getProperty(namespace+"HasId"), poi._id);

        if(poi.location.lat!=null)
            spot.addProperty(ontModel.getProperty(namespace+"HasLat"), poi.location.lat.toString());

        if(poi.location.lng!=null){
            spot.addProperty(ontModel.getProperty(namespace+"HasLng"), poi.location.lng.toString());
        }

        if(poi.description!=null){
            Resource description2 = ontModel.createResource(namespace+"Description-"+poi._id);
            description2.addProperty(RDF.type, "Description");
            description2.addProperty(RDFS.label, poi.description);
            spot.addProperty(ontModel.getProperty(namespace+"HasDescription"), description2);
        }




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
            parser.ontModel.write(fop,"N-TRIPLES");
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
