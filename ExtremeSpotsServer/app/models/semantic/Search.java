package models.semantic;


import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.query.*;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.function.library.namespace;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDF;

//import com.sun.tools.javac.util.Pair;
//import com.sun.xml.internal.ws.util.QNameMap;
import models.extremespots.StaticVariables;



/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 12/01/14
 * Time: 01:15
 * To change this template use File | Settings | File Templates.
 */
public class Search {

    private static final int PROPERTY = 0;
    private static final int CLASS = 1;
    private static final int INSTANCE = 2;

    private static String prefixos = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            +"PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            +"PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX fn:      <http://www.w3.org/2005/xpath-functions#>\n"
            + "PREFIX apf:     <http://jena.hpl.hp.com/ARQ/property#>\n"
            + "PREFIX dc:      <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX project:<http://www.semanticweb.org/joao/ontologies/2013/10/POIs#>\n";


    static public HashMap<String, ResultRecord> resultRecords;


    static public Model tdbModel;
    static public HashMap<Resource, Integer> resultados;
    static public HashMap<String, Integer> properties;
    static public ArrayList<Resource> finalResults;
    static public ArrayList<Resource> recResults;
    static public ArrayList<Resource> recKeyword;
    static public HashMap<String, String> soloProps;
    static public int terms;

    static public String namespace;

    static public ArrayList<String> searchClass;
    static public String[] termos;
    static public int recLimit = 20;
    static public ArrayList<String> browseArray;
    static public boolean vsON = false;
    static public Resource inst1 = null;
    static public Resource inst2 = null;



    ArrayList<String> keywords;

    public Search() {
        int count = 0;
        String inputQuery;
        QueryExecution qExec;

        namespace = StaticVariables.ontology_name_space;


        resultados = new HashMap();
        properties = new HashMap();
        finalResults = new ArrayList<Resource>();
        recKeyword = new ArrayList<Resource>();
        recResults = new ArrayList<Resource>();
        searchClass = new ArrayList<String>();
        soloProps = new HashMap();
        tdbModel = TDBFactory.createModel(StaticVariables.triple_store_directory);
        terms = 0;

    }



    public static int getWordCount() {
        int aux = 0;
        for (int i = 0; i < termos.length; i++) {
            if (termos[i] != null) {
                aux += 1;
            }
        }
        return aux;
    }



    public static ArrayList<Resource> keywordRecomendation() {
        //System.out.println(terms-soloProps.size()-properties.size());
        Set set = resultados.entrySet();
        ArrayList<Resource> auxResults = new ArrayList<Resource>();
        Iterator i = set.iterator();
        if (termos.length > 1) {
            while (i.hasNext()) {
                Map.Entry pair = (Map.Entry) i.next();
                if (getPairValue(pair) == getWordCount()-1) {
                    auxResults.add((Resource) pair.getKey());
                }
            }
        }
        return auxResults;
    }





    public static int getPairValue(Map.Entry pair){
        return Integer.parseInt(pair.getValue().toString());
    }



    public static ArrayList<Resource> returnResults() {
        System.out.println("\n\n\nResultados: "+resultados.size()+"\n");
        Set set = resultados.entrySet();
        ArrayList<Resource> auxResults = new ArrayList<Resource>();
        Iterator i = set.iterator();
        //System.out.println("termos = "+terms);
        while (i.hasNext()) {
            Map.Entry pair = (Map.Entry) i.next();
            //System.out.println(((Resource)pair.getKey()).getURI()+" - "+pair.getValue());
            if (getPairValue(pair) == terms) {
                auxResults.add((Resource) pair.getKey());
            }
        }
        return auxResults;
    }

    public static ArrayList<Resource> returnRec() {
        Set set = resultados.entrySet();
//        int count=0;
        ArrayList<Resource> auxResults = new ArrayList<Resource>();
        Iterator i = set.iterator();
//        for(int j = 0;j<termos.length;j++){
//            if(termos[j]!=null){
//                count++;
//            }
//        }
        //System.out.println(terms-count);
        while (i.hasNext()) {
            Map.Entry pair = (Map.Entry) i.next();
            if (getPairValue(pair) == terms) {
                auxResults.add((Resource) pair.getKey());
            }
        }
        return auxResults;
    }

    public static String getTermination(String uri) {
        int pos = uri.lastIndexOf("#");
        return pos != -1 ? uri.substring(pos + 1) : uri;
    }

    public static void addOrUpdate(Resource key) {
        if (resultados.containsKey(key)) {
            int value = resultados.get(key);
            resultados.put(key, value + 1);
        } else {
            resultados.put(key, 1);
        }
    }

    public static void addSolopropsPoints() {
        Set resultadosSet = resultados.entrySet();
        Set propertiesSet = soloProps.entrySet();
        Iterator resultadosIt = resultadosSet.iterator();
        while (resultadosIt.hasNext()) {
            Map.Entry pair = (Map.Entry) resultadosIt.next();
            Iterator propI = propertiesSet.iterator();
            while (propI.hasNext()) {
                Map.Entry propPair = (Map.Entry) propI.next();
                if (((Resource) pair.getKey()).getProperty(tdbModel.getProperty(namespace + propPair.getKey().toString())) != null) {
                    addOrUpdate((Resource) pair.getKey());
                }
            }
        }
    }



    public void combinationsArray(String s){
        String [] words = s.split(" ");
        ArrayList<String> frases = new ArrayList<String>();
        for(int i=0; i<(words.length*2-1);i++){
            frases.add("");
        }

        for(int i=0; i<words.length-1;i++){
            for(int j=0; j< words.length; j++) {
                frases.add(j, frases.get(j)+words[j]+" ");
                if(words[j].contains("'s")){
                    break;
                }
            }
        }
    }

    public ArrayList<MapPoint> search(String s) {


        //QuerySemanticProcessor

        QuerySemanticProcessor qp = new QuerySemanticProcessor(tdbModel,s);

        boolean current_part_is_a_class;
/*
        for(String part : s.split(" ")){
            boolean current_part_is_a_class = qp.findClasses(part);

             if(last_word_is_a_class)
                qp.is_this_a_mult_word_class(last_part, part);

            last_word_is_a_class = current_part_is_a_class;

        }*/

        for(String part : s.split(" ")){
            qp.findClasses(part);

        }

//        System.out.println("Find CLasses:");
//        for(String part : qp.sentence.classes)
        //           System.out.println(part);

        for(String part : s.split(" "))
            qp.findProperties(part);

        //  System.out.println("Find properties:");
        //  for(String prop : qp.sentence.properties)
        //     System.out.println(prop);


        for(String part : s.split(" "))
            qp.findInstancesByName(part);

        //  System.out.println("Find instances by name:");
        //  for(String part : qp.sentence.instances)
        //     System.out.println(part);

        qp.sentence.createQueries();

        if(qp.sentence.queries.size()>0){
            qp.runQueries();

        }
        System.out.println("***********************************");

        //contructFinalQuery(qp);


        //TreeMap<Resource,Integer> sorted_map = new TreeMap<Resource,Integer>(resultados);

        System.out.println(s);
        for(Map.Entry<Resource, Integer> entry : resultados.entrySet()) {
            Resource key = entry.getKey();
            Integer value = entry.getValue();
            if (key!=null){

                //System.out.println(key);
                //System.out.println(value);
                qp.getMapPointFromSparql(key.getURI());

            }
            //

            // do what you have to do here
            // In your case, an other loop.
        }

//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//
//        List<MapPoint> mapPoints = qp.mapPoints;
//        String arrayString = gson.toJson(mapPoints );
//
//        HashMap bindings = new HashMap();
//        bindings.put("bindings", arrayString);
//        String bindingsString = gson.toJson(bindings );
//
//
//        HashMap results = new HashMap();
//        results.put("results", bindingsString);

        return qp.mapPoints;



    }




    public void contructFinalQuery(QuerySemanticProcessor qp){

        for (int i = 0; i < qp.sentence.classes.size(); i++){
            String the_class = qp.sentence.classes.get(i);
            String the_class_query_word = qp.sentence.classesQueryWord.get(i);

            simple_query(the_class,"","");
            for(String the_property : qp.sentence.properties){
                simple_query(the_class,the_property,"");
                for(String the_instance : qp.sentence.instances){
                    simple_query(the_class,the_property,"");
                }
            }
        }
        for(String the_property : qp.sentence.properties){
            simple_query("",the_property,"");
            for(String the_instance : qp.sentence.instances){
                simple_query("",the_property,the_instance);
            }
        }
        for(String the_instance : qp.sentence.instances){
            simple_query("","",the_instance);
        }
    }

    public void simple_query(String the_class,String the_property, String the_instance){
        String inputQuery ="";
        inputQuery = prefixos + "SELECT distinct ?spot WHERE { \n";
        if(!the_class.isEmpty()){
            inputQuery = inputQuery + "?category rdfs:subClassOf* <"+the_class+">.\n"
                    + "?spot rdf:type ?category.\n";
        }
        if(!the_class.isEmpty()){
            inputQuery = inputQuery + "?spot project:"+the_property+" ?instance.\n";
        }
        if(!the_class.isEmpty()){
            inputQuery = inputQuery + "?spot project:"+the_property+" ?instance.\n";
        }
        inputQuery = inputQuery + "}\n";
        Query query;
        QueryExecution qExec;
        ResultSet results;
        //System.out.println("querystring: "+inputQuery);

        query = QueryFactory.create(inputQuery);
        qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        results = qExec.execSelect();
        int count = 0;

        //ResultSetFormatter.out(System.out, results, query);

        while (results.hasNext()) {
            QuerySolution qs = results.next();
            Resource node = qs.getResource("?spot");
            this.addOrUpdate(node);
            count++;
        }


    }




    public static void search2(String s) {

/*        QuerySemanticProcessor qp = new QuerySemanticProcessor(tdbModel,s);
        qp.detect_words();
        termos =  new String[qp.palavras.size()];
        for(int i=0; i < qp.palavras.size();i++){
            termos[i] = qp.palavras.get(i);

        }

        //termos = QuerySemanticProcessor.findQuotations(s);
        System.out.println("TERMOS");
        for(String t:termos){
            System.out.println(t.toString());
        }
        //QuerySemanticProcessor.findClass();
        //QuerySemanticProcessor.findProps();
        //findOps();

        recResults = returnRec();
        QuerySemanticProcessor.findTerms();

        QuerySemanticProcessor.contruct_final_query();*/

        addSolopropsPoints();
        recKeyword = keywordRecomendation();

        terms += soloProps.size(); //remover o numero de soloprops
        finalResults = returnResults();

        for (int m = 0; m < finalResults.size(); m++) {
            recKeyword.remove(finalResults.get(m));
            recResults.remove(finalResults.get(m));
        }

        for (int m = 0; m < recResults.size(); m++) {
            recKeyword.remove(recResults.get(m));
        }


        //System.out.println("terms "+terms);
        System.out.println("\n- Resultados -");


        if (soloProps.isEmpty()) {
            //System.out.println("solopros empty");

            for (int m = 0; m < finalResults.size(); m++) {
                //System.out.println("URI: " + getTermination(finalResults.get(m).getURI()));
                PoiToString(finalResults.get(m).getURI());
            }
        } else {
            System.out.println("solopros not empty= "+finalResults.size());

            for (int m = 0; m < finalResults.size(); m++) {

                System.out.print("URI: " + getTermination(finalResults.get(m).getURI()) + " ");
                Set set = soloProps.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry pair = (Map.Entry) i.next();
                    System.out.print("key: "+pair.getKey().toString()+" value: "+pair.getValue().toString()+" ");
                    if (finalResults.get(m).getProperty(tdbModel.getProperty(namespace + pair.getKey().toString())) != null) {
                        System.out.print(pair.getValue().toString() + ": " + finalResults.get(m).getProperty(tdbModel.getProperty(namespace + pair.getKey().toString())).getString() + " ");
                    } else {
                        //System.out.print(pair.getValue().toString()+": Not Available");
                    }
                }
                System.out.println();
            }
        }

        System.out.println("\n\n- Produtos Semelhantes -");
        if (recResults.isEmpty()) {
            System.out.println("Sem produtos semelhantes");
        } else {
            for (int m = 0; m < recResults.size() && m < recLimit; m++) {
                System.out.println("URI: " + getTermination(recResults.get(m).getURI()));
            }
        }


        System.out.println("\n\n- Outros Produtos -");
        if (recKeyword.isEmpty()) {
            System.out.println("Sem outros produtos disponiveis");
        } else {
            for (int m = 0; m < recKeyword.size() && m < recLimit; m++) {
                System.out.println("URI: " + getTermination(recKeyword.get(m).getURI()));
            }
        }
    }

    public static void PoiToString(String uri){
        String queryString = prefixos
        + "SELECT ?property ?resource\n"
        + "WHERE {\n"
        +    "<"+uri+"> ?property  ?resource.\n"
        + "}\n";

        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
        ResultSet results = qExec.execSelect();
        ResultSetFormatter.out(System.out,results,query);

    }

}
