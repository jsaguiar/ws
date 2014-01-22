package models.semantic;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 14/01/14
 * Time: 07:51
 * To change this template use File | Settings | File Templates.
 */
public class Sentence {


    private static String prefixos = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            +"PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            +"PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX fn:      <http://www.w3.org/2005/xpath-functions#>\n"
            + "PREFIX apf:     <http://jena.hpl.hp.com/ARQ/property#>\n"
            + "PREFIX dc:      <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX project:<http://www.semanticweb.org/joao/ontologies/2013/10/POIs#>\n";

    public String sentence;
    public ArrayList<String> classes;
    public ArrayList<String> properties;
    public ArrayList<String> instances;

    public ArrayList<String> classesQueryWord;
    public ArrayList<String> propertiesQueryWord;
    public ArrayList<String> instancesQueryWord;
    public ArrayList<Triples> triples;

    public ArrayList<String> queries;

    public Sentence(String sentence) {
        this.sentence = sentence;
        this.classes = new ArrayList<String>();
        this.properties = new ArrayList<String>();
        this.instances = new ArrayList<String>();
        this.classesQueryWord = new ArrayList<String>();
        this.propertiesQueryWord = new ArrayList<String>();
        this.instancesQueryWord = new ArrayList<String>();
        this.queries = new ArrayList<String>();
    }

    public void addClass(String the_class, String wordFromQuery){
        classesQueryWord.add(wordFromQuery);
        classes.add(the_class);
    }

    public void addProperty(String the_property, String wordFromQuery){
        propertiesQueryWord.add(wordFromQuery);
        properties.add(the_property);
    }

    public void addInstance(String the_instance, String wordFromQuery){
        instancesQueryWord.add(wordFromQuery);
        instances.add(the_instance);
    }

    public void searchQueries(){
        for (int i =0;i<classes.size();i++){
            createQuery(i,-1,-1);
            for (int j =0;j<properties.size();j++){
                createQuery(i,j,-1);
                for (int k =0;k<instances.size();k++){
                    createQuery(i,j,k);
                }
            }
        }
    }

    public void createQueries(){
        for (int i =0;i<classes.size();i++){
            createQuery(i,-1,-1);
            for (int j =0;j<properties.size();j++){
                createQuery(i,j,-1);
                for (int k =0;k<instances.size();k++){
                    createQuery(i,j,k);
                }
            }
        }
        for (int j =0;j<properties.size();j++){
            createQuery(-1,j,-1);
            for (int k =0;k<instances.size();k++){
                createQuery(-1,j,k);
            }
        }
        for (int k =0;k<instances.size();k++){
            createQuery(-1,-1,k);
        }
    }

    public void createQuery(int class_pos,int  property_pos, int instance_pos){
        String the_class = "",the_property = "", the_instance = "";
        String the_class_word = "",the_property_word = "", the_instance_word = "";
        String inputQuery ="";

        String user_query = sentence;

        String spot ="";
        inputQuery = prefixos + "SELECT distinct ?spot WHERE { \n";

        if (class_pos>=0){
            the_class = classes.get(class_pos);
            the_class_word = classesQueryWord.get(class_pos);
            spot = "?spot";
            user_query.replaceFirst(the_class_word, " ");
            inputQuery = inputQuery + "?category rdfs:subClassOf* <"+the_class+">.\n"
                    + spot+" rdf:type ?category.\n";


        } else if (instance_pos>=0){
            the_instance = instances.get(instance_pos);
            the_instance_word = instancesQueryWord.get(instance_pos);
            spot = "<"+the_instance+">";
            user_query.replaceFirst(the_instance_word, " ");
        }else{
            spot = "?spot";
            inputQuery = inputQuery + "?category rdfs:subClassOf* project:Spot.\n"
                    + spot+" rdf:type ?category.\n";

        }




        if (property_pos>=0){
            the_property = properties.get(property_pos);
            the_property_word = propertiesQueryWord.get(property_pos);
            user_query.replaceFirst(the_property_word, " ");
            inputQuery = inputQuery + spot+" project:"+the_property+" ?instance. FILTER regex(?instance,\"\\\\b" +user_query.toLowerCase()+ "\", \"i\")\n";

        }else{
            for (String part_user_query: user_query.split(" ")){
                inputQuery = inputQuery +"OPTIONAL{"+ spot+" ?property ?instance.  FILTER regex(?instance,\"\\\\b" +part_user_query.toLowerCase()+ "\", \"i\")}\n";
            }
        }

        //split the remaining query and query on these fields:

        //query the name of the spot
        if (property_pos>=0){

            for (String part_user_query: user_query.split(" ")){
                inputQuery = inputQuery +"OPTIONAL{"+ spot + " project:HasDescription ?description.\n";
                inputQuery = inputQuery +" ?description rdfs:label ?descriptionlabel.  FILTER regex(?descriptionlabel,\"\\\\b" +part_user_query.toLowerCase()+ "\", \"i\")}\n";
            }
        }
        //query the description of the spot

        inputQuery = inputQuery + "}\n";

        queries.add(inputQuery);


    }

    public void createSearchQuery(String query_other_parts){

        String inputQuery ="";

        String spot ="";
        inputQuery = prefixos + "SELECT distinct ?spot WHERE { \n OPTIONAL{";

        for(String the_class : classes){
            System.out.println("the_class");
            System.out.println(the_class);
            spot = "?spot";
            inputQuery = inputQuery +"OPTIONAL{" + "?category rdfs:subClassOf* <"+the_class+">.\n"
                    + spot+" rdf:type ?category.}\n";


        }

        for(String the_property : properties){
            System.out.println("");
            inputQuery = inputQuery +"OPTIONAL{"+ spot+" project:"+the_property+" ?instance. FILTER regex(?instance,\"" +query_other_parts.trim()+ "\", \"i\")}\n";

        }

        inputQuery = inputQuery +"} \n OPTIONAL{";

        for(String the_class : instances){
            spot = the_class;
            for(String the_property : properties){
                System.out.println("");
                inputQuery = inputQuery +"OPTIONAL{ <"+ spot+"> project:"+the_property+" ?instance. FILTER regex(?instance,\"" +query_other_parts.trim()+ "\", \"i\")}\n";

            }

        }

        inputQuery = inputQuery + "}}\n";

        //System.out.println(inputQuery);

        queries.add(inputQuery);
    }
}
