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
    String userQuestion = "";
    int foundPrint = 0;

public QuerySemanticProcessor(Model tdbModel, String query){
        this.tdbModel = tdbModel;
        separate = query.split(" ");

    }

    public void parse(){
        //ArrayList<String> keywords = identifyKeywords();
        //removeUnidentified(keywords);

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


        //return keywords;
    }
    public static ArrayList<String> identifyKeywords(){
        return new ArrayList<String>();
    }
    public static ArrayList<String> removeUnidentified(ArrayList<String> keywords){
        return keywords;
    }

    public void deleteFalseInstances()
    {
        int i, j;
        for (i = 0; i < palavras.size(); i++)
        {
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

                // Entao e classe ou propriedade
                if (results.hasNext())
                {
                    QuerySolution soln = results.nextSolution();

                    if (soln.get("x").asResource().getLocalName().toString().startsWith("has"))
                    {
                        palavras.add(toSearch);
                        names.add(soln.get("x").asResource().getLocalName().toString());
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



    public static String[] findQuotations(String searchInput) {
        String[] cleanQuery, auxQuery, tempQuery;
        String aux = "";
        int i, j = 0;
        tempQuery = searchInput.split(" ");
        auxQuery = new String[tempQuery.length];
        for (i = 0; i < tempQuery.length;) {
            if (tempQuery[i].startsWith("\"")) {
                aux += tempQuery[i].substring(1);
                i += 1;
                for (; i < tempQuery.length; i++) {
                    if (tempQuery[i].endsWith("\"")) {
                        aux += " " + tempQuery[i].substring(0, tempQuery[i].length() - 1);
                        auxQuery[j] = aux;
                        j += 1;
                        aux = "";
                        break;
                    } else {
                        aux += " " + tempQuery[i];
                    }
                }
                i += 1;
            } else {
                auxQuery[j] = tempQuery[i];
                i += 1;
                j += 1;
            }
        }
        cleanQuery = new String[j];
        for (i = 0; i < j; i++) {
            cleanQuery[i] = auxQuery[i];
        }
        return cleanQuery;
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


    public static void findProps() {
        String queryString;
        for (int i = 0; i < Search.termos.length; i++) {
            if (Search.termos[i] != null && Search.termos[i].compareToIgnoreCase("no") != 0) {
                queryString = prefixos + "SELECT ?x ?v WHERE { ?x rdfs:label ?v. FILTER regex(?v,\"" + Search.termos[i].toLowerCase() + "\",\"i\")}";
                com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
                QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
                ResultSet results = qExec.execSelect();
                //ResultSetFormatter.out(System.out,results,query);
                if (results.hasNext()) {
                    String prop = Search.getTermination(results.next().getResource("?x").getURI());
                    //System.out.println(termos[i]+" - "+prop);
                    if (isSoloProp(i)) {
                        Search.soloProps.put(prop, Search.termos[i]); //sendo soloprop ainda e uma condicao que o resultado final tem de ter, por isso n diminuo o terms
                    } else {
                        Search.properties.put(prop, i);
                        Search.terms += 1;
                    }
                    Search.termos[i] = null;

                    //break; //so aceito a primeira prop, se for ambiguo
                }
            } else {
                continue;
            }
            //ResultSetFormatter.out(System.out,results,query);

        }
    }

    public static boolean isSoloProp(int pos) {
        String op = null;
        String valor = null;
        if (Search.termos.length < 3) {
            return true;
        }
        if (Search.termos.length == 3 && pos == 1) {
            return true;
        }
        if (pos == (Search.termos.length - 1) || pos == (Search.termos.length - 2)) {
            if (Search.termos[pos - 1] != null && Search.termos[pos - 2] != null) {
                if (Search.termos[pos - 1].compareToIgnoreCase("=") == 0 || Search.termos[pos - 1].compareToIgnoreCase("<") == 0 || Search.termos[pos - 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
        } else if (pos == 0 || pos == 1) {
            if (Search.termos[pos + 1] != null && Search.termos[pos + 2] != null) {
                if (Search.termos[pos + 1].compareToIgnoreCase("=") == 0 || Search.termos[pos + 1].compareToIgnoreCase("<") == 0 || Search.termos[pos + 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
        } else {
            if (Search.termos[pos - 1] != null && Search.termos[pos - 2] != null) {
                if (Search.termos[pos - 1].compareToIgnoreCase("=") == 0 || Search.termos[pos - 1].compareToIgnoreCase("<") == 0 || Search.termos[pos - 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
            if (Search.termos[pos + 1] != null && Search.termos[pos + 2] != null) {
                if (Search.termos[pos + 1].compareToIgnoreCase("=") == 0 || Search.termos[pos + 1].compareToIgnoreCase("<") == 0 || Search.termos[pos + 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
        }
        return true;

    }//nokia os = windows os gps = yes gps 50 < height < 500


    public static void findClass() {
        String queryString;
        for (int i = 0; i < Search.termos.length; i++) {//nao preciso de verificar por null pq e a primeira pesquisa
            queryString = prefixos + "SELECT ?spot ?label WHERE { \n" //+?x rdfs:label ?v. FILTER regex(?v,\"" + termos[i].toLowerCase() + "\",\"i\")}";
                    +"?category rdfs:subClassOf* project:Spot.\n"
                    +"       ?spot rdf:type ?category.\n"
                    +"      ?category rdfs:label ?label. FILTER regex(?label,\"" +Search.termos[i].toLowerCase()+ "\", \"i\")\n"
                    +"}\n";

            //System.out.println("querystring: "+queryString);
            Query query = QueryFactory.create(queryString);
            QueryExecution qExec = QueryExecutionFactory.create(query, Search.tdbModel);
            ResultSet results = qExec.execSelect();
            //ResultSetFormatter.out(System.out,results,query);
            if (results.hasNext()) {
                String temp = results.next().getResource("?spot").getProperty(RDF.type).getObject().toString();
                Search.searchClass.add(temp);
                Search.termos[i] = null;
                break;
            }
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
}
