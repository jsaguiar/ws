package com.ws.data;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ws.extremespots.StaticVariables;

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
        Resource spot = ontModel.createResource(namespace+poi.name,ontModel.getResource(namespace+category));
        spot.addProperty(RDF.type, category);

        String description = (poi.description!=null)? poi.description :"";
        spot.addProperty(ontModel.getProperty(namespace+"Description"), description);

        String contact = (poi.contact.phone!=null)? poi.contact.phone :"";
        spot.addProperty(ontModel.getProperty(namespace+"Contact"), contact);

        String id = (poi.id!=null)? poi.id :"";
        spot.addProperty(ontModel.getProperty(namespace+"Id"), id);

        //String lat = (poi.id!=null)? poi.id :"";
        //spot.addProperty(ontModel.getProperty(namespace+"Lat"), lat);

        //String lng = (poi.ln!=null)? poi.lng :"";
        //spot.addProperty(ontModel.getProperty(namespace+"Lng"), lng);





    }
    public static void main (String args[]) {
        Parser parser = new Parser();


    }

}
