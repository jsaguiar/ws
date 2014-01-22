package com.ws.semantic;


import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 12/01/14
 * Time: 01:21
 * To change this template use File | Settings | File Templates.
 */
public class QuerySemanticProcessor {

    public ArrayList<MapPoint> mapPoints;


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

    private Model tdbModel;
    private String[] separate;
    //private ArrayList<Counted> counts;
    ArrayList<String> palavras = new ArrayList<String>();
    ArrayList<Integer> means = new ArrayList<Integer>();
    ArrayList<String> names = new ArrayList<String>();
    //ArrayList<Return> resultsList = new ArrayList<Return>();


    public Sentence sentence;

    String userQuestion = "";
    int foundPrint = 0;

    public QuerySemanticProcessor(Model tdbModel, String query){
        this.tdbModel = tdbModel;
        separate = query.split(" ");
        sentence = new Sentence(query);
        mapPoints = new ArrayList<MapPoint>();
    }

    public void detect_words(){
        int i;


        // Divide a frase nas suas partes
        checker();

        // Apaga instancias que estejam a mais
        deleteFalseInstances();

        // DEBUG PRINT

        for (i = 0; i < palavras.size(); i++)
        {
            System.out.println(palavras.get(i) + " " + means.get(i));
        }

        for (String part :palavras){
            sentence.sentence = sentence.sentence+palavras;
        }

    }


    public void deleteFalseInstances()
    {
        int i, j;
        for (i = 0; i < palavras.size(); i++)
        {

            if (means.get(i) == CLASS)
            {
                System.out.println("asdassdas");
                for (j = i + 1; j < palavras.size(); j++)
                {
                    if (palavras.get(i).contains(palavras.get(j)) && means.get(j) == CLASS )
                    {
                        palavras.remove(i);
                        means.remove(i);
                        names.remove(i);
                        i--;
                    } else if (palavras.get(j).contains(palavras.get(i)) && means.get(j) == CLASS )
                    {
                        palavras.remove(j);
                        means.remove(j);
                        names.remove(j);
                    }else{
                        break;
                    }
                }
            }

            if (means.get(i) == INSTANCE)
            {
                for (j = i + 1; j < palavras.size(); j++)
                {
                    if (palavras.get(i).contains(palavras.get(j)) && (means.get(j) == PROPERTY || means.get(j) == CLASS))
                    {
                        palavras.remove(i);
                        means.remove(i);
                        names.remove(i);
                        i--;
                        break;
                    }
                }
            }


        }
    }

    // Verifica o que representa cada termo da pesquisa
    public void checker()
    {
        int i, j;
        String queryString = "";
        String toSearch = "";

        for (i = 0; i < separate.length; i++)
        {
            for (j = i; j < separate.length; j++)
            {
                if (i == j)
                    toSearch = separate[i];

                else
                    toSearch = toSearch.concat(" ").concat(separate[j]);

                queryString = prefixos + "SELECT DISTINCT ?x ?type " + "WHERE { ?x rdfs:label ?label . " + " ?x rdf:type ?type . " + "FILTER(?label='"
                        + toSearch + "'^^<http://www.w3.org/2001/XMLSchema#string>) " + ".}";

                Query query = QueryFactory.create(queryString);
                QueryExecution qexec = QueryExecutionFactory.create(query, tdbModel);
                ResultSet results = qexec.execSelect();
                ResultSetFormatter.out(System.out,results,query);

                // Entao e classe ou propriedade
                if (results.hasNext())
                {
                    QuerySolution soln = results.nextSolution();

                    if (soln.get("x").asResource().getLocalName().toString().startsWith("has"))
                    {
                        palavras.add(toSearch);
                        System.out.println(soln.get("x").asResource().getLocalName().toString());
                        means.add(PROPERTY);
                    }
                    else
                    {
                        palavras.add(toSearch);
                        names.add(soln.get("x").asResource().getLocalName().toString());

                        means.add(CLASS);
                    }
                    break;
                }

                // Nao e classe nem propriedade
                else
                {
                    palavras.add(toSearch);

                    names.add("");
                    means.add(INSTANCE);
                }
            }
        }
    }





    public static void findTerms() {
        String inputQuery;
        Query query;
        QueryExecution qExec;
        ResultSet results;
        int count = 0;
        for (int i = 0; i < Search.termos.length; i++) {
            if (Search.termos[i] != null) {
                Search.terms += 1;
                if (Search.searchClass.size() > 0) {
                    inputQuery = prefixos + "SELECT distinct ?spot WHERE { \n";
                    for(String classe : Search.searchClass){
                        inputQuery = inputQuery +"<"+ classe + " > ?property  ?resource.";
                    }
                    inputQuery = inputQuery + "}\n";
                } else {
                    inputQuery = prefixos + "SELECT distinct ?spot WHERE { ?x ?p ?t. FILTER regex(?t,\"" + Search.termos[i].toLowerCase() + "\",\"i\")}";
                }

                query = QueryFactory.create(inputQuery);
                qExec = QueryExecutionFactory.create(query, Search.tdbModel);
                results = qExec.execSelect();
                //ResultSetFormatter.out(System.out,results,query);
                while (results.hasNext()) {
                    QuerySolution qs = results.next();
                    Resource node = qs.getResource("?spot");
                    Search.addOrUpdate(node);
                    count++;
                }
                System.out.println("Resultados de " + Search.termos[i] + ": " + count);
                count = 0;
            }
        }
    }

    public static void findOps() {
        int temp = 0;
        String queryString;
        Set set = Search.properties.entrySet();
        Iterator i = set.iterator();
        String op = null;
        String valor = null;

        while (i.hasNext()) {
            Map.Entry pair = (Map.Entry) i.next();//testar se ta no limite direito ou esquerdo e chamar as funcoes de acordo
            if (getPairValue(pair) == 0 || getPairValue(pair) == 1) {
                if (Search.termos.length == 3 && getPairValue(pair) != 0) {
                    continue;
                }
                testRight(pair);
            } else if (getPairValue(pair) == Search.termos.length - 1 || getPairValue(pair) == Search.termos.length - 2) {
                if (Search.termos.length == 3 && getPairValue(pair) != 2) {
                    continue;
                }
                testLeft(pair);
            } else {

                if (testRight(pair)) {
                    temp += 1;
                }
                if (testLeft(pair)) {
                    temp += 1;
                }
                if (temp == 2) {
                    Search.terms += 1;
                }//nokia os = windows os gps = yes gps 50 < height < 500
            }
        }
    }
    public static boolean testRight(Map.Entry pair) {
        String op = null;
        String valor = null;
        String queryString;
        Search.termos[getPairValue(pair)] = null;
        if (getPairValue(pair) == (Search.termos.length - 1) || getPairValue(pair) == (Search.termos.length - 2)) {
            return false;
        }
        if (Search.termos[getPairValue(pair) + 1] != null && Search.termos[getPairValue(pair) + 2] != null) {
            if (Search.termos[getPairValue(pair) + 1].compareToIgnoreCase("=") == 0 || Search.termos[getPairValue(pair) + 1].compareToIgnoreCase("<") == 0 || Search.termos[getPairValue(pair) + 1].compareToIgnoreCase(">") == 0) {
                //se encontrar tenho de por nos termos a propriedade, valor e operacao a null para nao serem pesquisados novamente
                valor = Search.termos[getPairValue(pair) + 2];
                Search.termos[getPairValue(pair) + 2] = null;
                op = Search.termos[getPairValue(pair) + 1];
                Search.termos[getPairValue(pair) + 1] = null;
                // posso por a null pk tenho a prop no key do pair. (nao posso por ja a null porque pode ter 5 < camara < 8)
                //terms-=1; //apesar de por 3 a null, os termos so subtraio 1 porque a pesquisa "camara > 5" vai dar um termo e ja subtrai um na funcao findprops
                //soloProps.remove(pair.getKey().toString()); //ja nao é soloprop
            } //se op for = uso regex, senao uso normal < e >
        }
        if (op != null && valor != null) {
            if (op.compareTo("=") != 0) {
                queryString = prefixos + "SELECT distinct ?x WHERE { ?x uri:" + pair.getKey().toString() + " ?v . FILTER (?v " + op + " " + valor + ")}";
            } else {
                try {
                    float f = Float.valueOf(valor).floatValue();
                    queryString = prefixos + "SELECT distinct ?x WHERE { ?x uri:" + pair.getKey().toString() + " ?v . FILTER (?v = " + valor + ")}";
                } catch (NumberFormatException nfe) {
                    queryString = prefixos+ "SELECT distinct ?x WHERE { ?x uri:" + pair.getKey().toString() + " ?v . FILTER regex(?v,\"" + valor + "\",\"i\")}";
                }

            }
            com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
            QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
            ResultSet results = qExec.execSelect();
            //System.out.println("query: "+queryString);
            //aaResultSetFormatter.out(System.out,results,query);
            while (results.hasNext()) {
                QuerySolution qs = results.next();
                Resource node = qs.getResource("?x");
                Search.addOrUpdate(node);
            }
            //ResultSetFormatter.out(System.out,results,query);
            return true;
        } else {
            return false;
        }
    }

    public static boolean testLeft(Map.Entry pair) {
        String op = null;
        String valor = null;
        String queryString;
        Search.termos[getPairValue(pair)] = null;
        if (getPairValue(pair) == 0 || getPairValue(pair) == 1) {
            return false;
        }
        if (Search.termos[getPairValue(pair) - 1] != null && Search.termos[getPairValue(pair) - 2] != null) {
            if (Search.termos[getPairValue(pair) - 1].compareToIgnoreCase("=") == 0 || Search.termos[getPairValue(pair) - 1].compareToIgnoreCase("<") == 0 || Search.termos[getPairValue(pair) - 1].compareToIgnoreCase(">") == 0) {
                //se encontrar tenho de por nos termos a propriedade, valor e operacao a null para nao serem pesquisados novamente
                valor = Search.termos[getPairValue(pair) - 2];
                Search.termos[getPairValue(pair) - 2] = null;
                op = Search.termos[getPairValue(pair) - 1];
                Search.termos[getPairValue(pair) - 1] = null;
                //termos[(int)pair.getValue()] = null; // posso por a null pk tenho a prop no key do pair. (nao posso por ja a null porque pode ter 5 < camara < 8)
                //terms-=1; //apesar de por 3 a null, os termos so subtraio 1 porque a pesquisa "camara > 5" vai dar um termo e ja subtrai um na funcao findprops
                //soloProps.remove(pair.getKey().toString()); //ja nao é soloprop
            } //se op for = uso regex, senao uso normal < e >
        }
        if (op != null && valor != null) {
            if (op.compareTo("=") != 0) {
                queryString = prefixos + "SELECT distinct ?x WHERE { ?x uri:" + pair.getKey().toString() + " ?v . FILTER (" + valor + " " + op + " ?v)}";
            } else {
                try {
                    float f = Float.valueOf(valor).floatValue();
                    queryString = prefixos + "SELECT distinct ?x WHERE { ?x uri:" + pair.getKey().toString() + " ?v . FILTER (?v = " + valor + ")}";
                } catch (NumberFormatException nfe) {
                    queryString = prefixos + "SELECT distinct ?x WHERE { ?x uri:" + pair.getKey().toString() + " ?v . FILTER regex(?v,\"" + valor + "\",\"i\")}";
                }
            }
            com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
            QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
            ResultSet results = qExec.execSelect();
            //System.out.println("query: "+queryString);
            //ResultSetFormatter.out(System.out,results,query);
            while (results.hasNext()) {
                QuerySolution qs = results.next();
                Resource node = qs.getResource("?x");
                Search.addOrUpdate(node);
            }
            //ResultSetFormatter.out(System.out,results,query);
            return true;
        } else {
            return false;
        }
    }


    public void findProperties(String sentence_part) {
        String queryString;
        queryString = prefixos;
        queryString = queryString +"SELECT distinct ?property ?label WHERE {"+
                "?subject ?property ?object."+
                "?property rdfs:label ?label. FILTER regex(?label,\"\\\\b"+ sentence_part +"\",\"i\")." +
                "}";
        com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        ResultSet results = qExec.execSelect();
        //ResultSetFormatter.out(System.out,results,query);
        while(results.hasNext()) {
            String temp = Search.getTermination(results.next().getResource("?property").getURI());
            //System.out.println(termos[i]+" - "+prop);
            sentence.addProperty(temp, sentence_part);


            //break; //so aceito a primeira prop, se for ambiguo
        }

    }

    public void findClasses( String sentence_part) {
        String queryString;
        queryString = prefixos + "SELECT distinct ?category WHERE { \n" //+?x rdfs:label ?v. FILTER regex(?v,\"" + termos[i].toLowerCase() + "\",\"i\")}";
                +"?category rdfs:subClassOf* project:Spot.\n"
                +"       ?spot rdf:type ?category.\n"
                +"      ?category rdfs:label ?label. FILTER regex(?label,\"\\\\b" +sentence_part.toLowerCase()+ "\", \"i\")\n"
                +"}\n";

        //System.out.println("querystring: "+queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        ResultSet results = qExec.execSelect();
        //ResultSetFormatter.out(System.out,results,query);
        while(results.hasNext()) {
            String temp = results.next().getResource("?category").getURI();
            //Search.searchClass.add(temp);
            sentence.addClass(temp, sentence_part);
        }
    }
    public void findMostSpecificClasses(Sentence sentence, String classe) {
        //se existir palavras compostas, e for filha de alguma classe elimina a classe mae
        //split -> da classe,
        // if class != class[j]
        // if parts[i] in sentence's classes
        //ver  se uma é filha da outra
    }

    public void findInstances(String sentence_part) {
        //se existir alguma instancia menos abrangente
        String queryString;
        queryString = prefixos + "SELECT ?spot ?label WHERE { \n"
                +"?category rdfs:subClassOf* project:Spot.\n"
                +"       ?spot rdf:type ?category.\n"
                +"      ?category rdfs:label ?label. FILTER regex(?label,\"\\\\b" +sentence_part.toLowerCase()+ "\", \"i\")\n"
                +"}\n";

        //System.out.println("querystring: "+queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        ResultSet results = qExec.execSelect();
        //ResultSetFormatter.out(System.out,results,query);
        while(results.hasNext()) {
            String temp = results.next().getResource("?spot").getURI();
            sentence.addInstance(temp, sentence_part);
        }


    }


    public void findInstancesByName(String sentence_part) {
        String queryString;
        queryString = prefixos + "SELECT ?spot ?label WHERE { \n" //+?x rdfs:label ?v. FILTER regex(?v,\"" + termos[i].toLowerCase() + "\",\"i\")}";
                +"?category rdfs:subClassOf* project:Spot.\n"
                +"       ?spot rdf:type ?category.\n"
                +"      ?spot project:HasName ?label. FILTER regex(?label,\"\\\\b" +sentence_part.toLowerCase()+ "\", \"i\")\n"
                +"}\n";

        //System.out.println("querystring: "+queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        ResultSet results = qExec.execSelect();
        //ResultSetFormatter.out(System.out,results,query);
        while(results.hasNext()) {
            String temp = results.next().getResource("?spot").getURI();
            sentence.addInstance(temp, sentence_part);
        }
    }


    public void findMostSpecificInstances(Sentence sentence, String sentence_part) {
        //se for filha de alguma classe elimina a classe mae
    }

    public void findProperties(Sentence sentence, String sentence_part) {
        String queryString;
        queryString = prefixos + "SELECT ?spot ?label WHERE { \n" //+?x rdfs:label ?v. FILTER regex(?v,\"" + termos[i].toLowerCase() + "\",\"i\")}";
                +"?category rdfs:subClassOf* project:Spot.\n"
                +"       ?spot rdf:type ?category.\n"
                +"      ?category rdfs:label ?label. FILTER regex(?label,\"\\\\b" +sentence.sentence.toLowerCase()+ "\", \"i\")\n"
                +"}\n";

        //System.out.println("querystring: "+queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        ResultSet results = qExec.execSelect();
        //ResultSetFormatter.out(System.out,results,query);
        if (results.hasNext()) {
            String temp = results.next().getResource("?spot").getProperty(RDF.type).getObject().toString();
            Search.searchClass.add(temp);
            sentence.classes.add(temp);
        }

    }

    public static int getPairValue(Map.Entry pair){
        return Integer.parseInt(pair.getValue().toString());
    }


    public static void contruct_final_query(){
        String inputQuery ="";
        inputQuery = prefixos + "SELECT distinct ?spot WHERE { \n";
        for(String classe : Search.searchClass){
            inputQuery = inputQuery + "?category rdfs:subClassOf* <"+classe+">.\n"
                    + "?spot rdf:type ?category.\n";
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
            Search.addOrUpdate(node);
            count++;
        }

    }


    public boolean is_this_a_mult_word_class(String last_word, String current_word)
    {
        return false;

    }

    public void runQueries(){

        for (String sparqlQuery:sentence.queries){
            try{
            //System.out.println(sparqlQuery);
            Query query;
            QueryExecution qExec;
            ResultSet results;

            query = QueryFactory.create(sparqlQuery);
            qExec = QueryExecutionFactory.create(query, Search.tdbModel);
            results = qExec.execSelect();

            int count = 0;
                //ResultSetFormatter.out(System.out, results, query);

            while (results.hasNext()) {
                QuerySolution qs = results.next();
                Resource node = qs.getResource("?spot");
                Search.addOrUpdate(node);
                count++;
            }
            }catch (Exception e){
                System.out.println(sparqlQuery);
                return;
            }
        }

    }


    public void getMapPointFromSparql(String spot_uri) {
        String queryString;

        queryString = prefixos + "SELECT ?name ?id ?lat ?lng ?description ?address ?price ?rating WHERE { "
                + "<"+spot_uri+"> project:HasId  ?id . "
                + "<"+spot_uri+"> project:HasName ?name."
                + "<"+spot_uri+"> project:HasLat ?lat."
                + "<"+spot_uri+"> project:HasLng ?lng."
                +"OPTIONAL{ <"+spot_uri+"> project:HasDescription ?desc. ?desc rdfs:label ?description.}"
                + "<"+spot_uri+"> project:HasPrice ?price."
                + "<"+spot_uri+"> project:HasPrice ?address."
                + "<"+spot_uri+"> project:HasRating ?rating."
                +"}\n";

        //System.out.println("querystring: "+queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
        ResultSet results = qExec.execSelect();

        //ResultSetFormatter.outputAsJSON(outStream,results);
        //System.out.println(results.g);
        if (results.hasNext()) {
            QuerySolution next = results.next();
            String id = next.getLiteral("?id").toString();
            String name = next.getLiteral("?name").toString();
            String lat = next.getLiteral("?lat").toString();
            String lng = next.getLiteral("?lng").toString();
            String description = next.getLiteral("?description").toString();
            String price = next.getLiteral("?price").toString();
            String address = next.getLiteral("?address").toString();
            String rating = next.getLiteral("?rating").toString();
            mapPoints.add(new MapPoint(id, name,lat, lng, description, price, address, rating));
        }

    }


}
