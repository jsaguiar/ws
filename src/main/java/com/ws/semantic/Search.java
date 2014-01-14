package com.ws.semantic;


import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.query.*;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.function.library.namespace;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ws.extremespots.StaticVariables;



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

    private static String prefixos = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>" + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>" + "PREFIX fn:      <http://www.w3.org/2005/xpath-functions#>" + "PREFIX apf:     <http://jena.hpl.hp.com/ARQ/property#>" + "PREFIX dc:      <http://purl.org/dc/elements/1.1/>" + "PREFIX project:<http://www.semanticweb.org/joao/ontologies/2013/10/POIs#> ";


    static public Model tdbModel;
    static public HashMap<Resource, Integer> resultados;
    static public HashMap<String, Integer> properties;
    static public ArrayList<Resource> finalResults;
    static public ArrayList<Resource> recResults;
    static public ArrayList<Resource> recKeyword;
    static public HashMap<String, String> soloProps;
    static public int terms;

    static public String namespace;

    static public String searchClass = null;
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
        soloProps = new HashMap();
        tdbModel = TDBFactory.createModel(StaticVariables.triple_store_directory);
        terms = 0;

    }

    public static void search(String input) {
        searchWithoutVS(input);

    }

    public static ArrayList<Resource> getObjectsFromBrowse(String input) {
        //
        ArrayList<Resource> results = new ArrayList<Resource>();
        com.hp.hpl.jena.query.Query query;
        QueryExecution qExec;
        ResultSet bresults;
        String queryString = prefixos + "SELECT ?x WHERE { ?x rdf:type \"" + input.toLowerCase() + "\" . }";
        query = QueryFactory.create(queryString);
        qExec = QueryExecutionFactory.create(query, tdbModel);
        bresults = qExec.execSelect();
        while (bresults.hasNext()) {
            QuerySolution qs = bresults.next();
            Resource node = qs.getResource("?x");
            results.add(node);
        }
        return results;
    }

    public static void findAndPrintSubClasses(int tabnum, String classname) {
        String tabs = "";
        for (int i = 0; i < tabnum; i++) {
            tabs += "   ";
        }
        String queryString = prefixos +  "SELECT ?x WHERE { ?x rdfs:subClassOf uri:" + classname + " . }";
        com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
        ResultSet results = qExec.execSelect();
        browseArray.add(tabs + classname.substring(0, 1).toUpperCase() + classname.substring(1));
        while (results.hasNext()) {
            QuerySolution qs = results.next();
            String subclass = getTermination(qs.getResource("?x").getURI());
            findAndPrintSubClasses(tabnum + 1, subclass);
        }
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

    //MELHORIAS
    //se procurar por "nokia os" so me mostra resultados que tenham OS como propriedade, ja nao mostra baterias e carregadores
    //tudo e adaptavel a outras ontologias
    //semantic browsing e recursivo e adaptavel a outras ontologias
    public static void getBrowsing() {
        ArrayList<String> topClasses = new ArrayList<String>();
        browseArray = new ArrayList<String>();
        //todas as classes que nao sao subclasses
        String queryString = prefixos+  "SELECT * WHERE { ?x rdf:type rdfs:Class . FILTER NOT EXISTS { ?x rdfs:subClassOf ?t .} }";
        com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
        QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
        ResultSet results = qExec.execSelect();
        while (results.hasNext()) {
            QuerySolution qs = results.next();
            String classname = getTermination(qs.getResource("?x").getURI());
            findAndPrintSubClasses(0, classname);
        }
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


    public static boolean testLeft(Map.Entry pair) {
        String op = null;
        String valor = null;
        String queryString;
        termos[getPairValue(pair)] = null;
        if (getPairValue(pair) == 0 || getPairValue(pair) == 1) {
            return false;
        }
        if (termos[getPairValue(pair) - 1] != null && termos[getPairValue(pair) - 2] != null) {
            if (termos[getPairValue(pair) - 1].compareToIgnoreCase("=") == 0 || termos[getPairValue(pair) - 1].compareToIgnoreCase("<") == 0 || termos[getPairValue(pair) - 1].compareToIgnoreCase(">") == 0) {
                //se encontrar tenho de por nos termos a propriedade, valor e operacao a null para nao serem pesquisados novamente
                valor = termos[getPairValue(pair) - 2];
                termos[getPairValue(pair) - 2] = null;
                op = termos[getPairValue(pair) - 1];
                termos[getPairValue(pair) - 1] = null;
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
            QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
            ResultSet results = qExec.execSelect();
            //System.out.println("query: "+queryString);
            //ResultSetFormatter.out(System.out,results,query);
            while (results.hasNext()) {
                QuerySolution qs = results.next();
                Resource node = qs.getResource("?x");
                addOrUpdate(node);
            }
            //ResultSetFormatter.out(System.out,results,query);
            return true;
        } else {
            return false;
        }
    }


    public static int getPairValue(Map.Entry pair){
        return Integer.parseInt(pair.getValue().toString());
    }

    public static boolean testRight(Map.Entry pair) {
        String op = null;
        String valor = null;
        String queryString;
        termos[getPairValue(pair)] = null;
        if (getPairValue(pair) == (termos.length - 1) || getPairValue(pair) == (termos.length - 2)) {
            return false;
        }
        if (termos[getPairValue(pair) + 1] != null && termos[getPairValue(pair) + 2] != null) {
            if (termos[getPairValue(pair) + 1].compareToIgnoreCase("=") == 0 || termos[getPairValue(pair) + 1].compareToIgnoreCase("<") == 0 || termos[getPairValue(pair) + 1].compareToIgnoreCase(">") == 0) {
                //se encontrar tenho de por nos termos a propriedade, valor e operacao a null para nao serem pesquisados novamente
                valor = termos[getPairValue(pair) + 2];
                termos[getPairValue(pair) + 2] = null;
                op = termos[getPairValue(pair) + 1];
                termos[getPairValue(pair) + 1] = null;
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
            QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
            ResultSet results = qExec.execSelect();
            //System.out.println("query: "+queryString);
            //aaResultSetFormatter.out(System.out,results,query);
            while (results.hasNext()) {
                QuerySolution qs = results.next();
                Resource node = qs.getResource("?x");
                addOrUpdate(node);
            }
            //ResultSetFormatter.out(System.out,results,query);
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<Resource> returnResults() {
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

    public static void findTerms() {
        String inputQuery;
        Query query;
        QueryExecution qExec;
        ResultSet results;
        int count = 0;
        for (int i = 0; i < termos.length; i++) {
            if (termos[i] != null) {
                terms += 1;
                if (searchClass != null) {
                    inputQuery = prefixos + "SELECT distinct ?x WHERE { ?x ?p ?t. ?x rdf:type \"" + searchClass.toLowerCase() + "\". FILTER regex(?t,\"" + termos[i] + "\",\"i\")}";
                } else {
                    inputQuery = prefixos + "SELECT distinct ?x WHERE { ?x ?p ?t. FILTER regex(?t,\"" + termos[i] + "\",\"i\")}";
                }

                query = QueryFactory.create(inputQuery);
                qExec = QueryExecutionFactory.create(query, tdbModel);
                results = qExec.execSelect();
                //ResultSetFormatter.out(System.out,results,query);
                while (results.hasNext()) {
                    QuerySolution qs = results.next();
                    Resource node = qs.getResource("?x");
                    addOrUpdate(node);
                    count++;
                }
                System.out.println("Resultados de " + termos[i] + ": " + count);
                count = 0;
            }
        }
    }

    public static void findOps() {
        int temp = 0;
        String queryString;
        Set set = properties.entrySet();
        Iterator i = set.iterator();
        String op = null;
        String valor = null;

        while (i.hasNext()) {
            Map.Entry pair = (Map.Entry) i.next();//testar se ta no limite direito ou esquerdo e chamar as funcoes de acordo
            if (getPairValue(pair) == 0 || getPairValue(pair) == 1) {
                if (termos.length == 3 && getPairValue(pair) != 0) {
                    continue;
                }
                testRight(pair);
            } else if (getPairValue(pair) == termos.length - 1 || getPairValue(pair) == termos.length - 2) {
                if (termos.length == 3 && getPairValue(pair) != 2) {
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
                    terms += 1;
                }//nokia os = windows os gps = yes gps 50 < height < 500
            }
        }
    }


    public static void findProps() {
        String queryString;
        for (int i = 0; i < termos.length; i++) {
            if (termos[i] != null && termos[i].compareToIgnoreCase("no") != 0) {
                queryString = prefixos + "SELECT ?x ?v WHERE { ?x rdfs:label ?v. FILTER regex(?v,\"" + termos[i] + "\",\"i\")}";
                com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
                QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
                ResultSet results = qExec.execSelect();
                //ResultSetFormatter.out(System.out,results,query);
                if (results.hasNext()) {
                    String prop = getTermination(results.next().getResource("?x").getURI());
                    //System.out.println(termos[i]+" - "+prop);
                    if (isSoloProp(i)) {
                        soloProps.put(prop, termos[i]); //sendo soloprop ainda e uma condicao que o resultado final tem de ter, por isso n diminuo o terms
                    } else {
                        properties.put(prop, i);
                        terms += 1;
                    }
                    termos[i] = null;

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
        if (termos.length < 3) {
            return true;
        }
        if (termos.length == 3 && pos == 1) {
            return true;
        }
        if (pos == (termos.length - 1) || pos == (termos.length - 2)) {
            if (termos[pos - 1] != null && termos[pos - 2] != null) {
                if (termos[pos - 1].compareToIgnoreCase("=") == 0 || termos[pos - 1].compareToIgnoreCase("<") == 0 || termos[pos - 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
        } else if (pos == 0 || pos == 1) {
            if (termos[pos + 1] != null && termos[pos + 2] != null) {
                if (termos[pos + 1].compareToIgnoreCase("=") == 0 || termos[pos + 1].compareToIgnoreCase("<") == 0 || termos[pos + 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
        } else {
            if (termos[pos - 1] != null && termos[pos - 2] != null) {
                if (termos[pos - 1].compareToIgnoreCase("=") == 0 || termos[pos - 1].compareToIgnoreCase("<") == 0 || termos[pos - 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
            if (termos[pos + 1] != null && termos[pos + 2] != null) {
                if (termos[pos + 1].compareToIgnoreCase("=") == 0 || termos[pos + 1].compareToIgnoreCase("<") == 0 || termos[pos + 1].compareToIgnoreCase(">") == 0) {
                    return false;
                }
            }
        }
        return true;

    }//nokia os = windows os gps = yes gps 50 < height < 500


    public static void findClass() {
        String queryString;
        for (int i = 0; i < termos.length; i++) {//nao preciso de verificar por null pq e a primeira pesquisa
            queryString = prefixos + "SELECT ?x WHERE { ?x rdf:type \"" + termos[i].toLowerCase() + "\". ?x rdf:type ?t}";
            //System.out.println("querystring: "+queryString);
            com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
            QueryExecution qExec = QueryExecutionFactory.create(query, tdbModel);
            ResultSet results = qExec.execSelect();
            //ResultSetFormatter.out(System.out,results,query);
            if (results.hasNext()) {
                String temp = results.next().getResource("?x").getProperty(RDF.type).getObject().toString();
                searchClass = getTermination(temp);
                //System.out.println("searchclass "+searchClass);
                termos[i] = null;
                //terms--;
                //classIgnorePos = i;
                break;
            }
            //ResultSetFormatter.out(System.out,results,query);

        }
    }

    public static void searchWithoutVS(String s) {

        //QuerySemanticProcessor

        termos = findQuotations(s);
        System.out.println("TERMOS");
        for(String t:termos){
            System.out.println(t.toString());
        }
        findClass();
        findProps();
        findOps();
        recResults = returnRec();
        findTerms();
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


        if (finalResults.size() == 1 && inst1 == null) {
            inst1 = finalResults.get(0);

        } else if (finalResults.size() == 1 && inst2 == null) {
            inst2 = finalResults.get(0);
        } else {
            System.out.println("nao da para comparar");
        }

        //System.out.println("terms "+terms);
        System.out.println("\n- Resultados -");


        if (soloProps.isEmpty()) {
            System.out.println("solopros empty");

            for (int m = 0; m < finalResults.size(); m++) {
                System.out.println("URI: " + getTermination(finalResults.get(m).getURI()));
            }
        } else {
            System.out.println("solopros not empty");

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

}
