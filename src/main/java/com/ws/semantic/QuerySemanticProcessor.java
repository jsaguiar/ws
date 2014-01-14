package com.ws.semantic;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import java.util.ArrayList;

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

    private static String prefixos = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>" + "PREFIX owl:     <http://www.w3.org/2002/07/owl#>" + "PREFIX fn:      <http://www.w3.org/2005/xpath-functions#>" + "PREFIX apf:     <http://jena.hpl.hp.com/ARQ/property#>" + "PREFIX dc:      <http://purl.org/dc/elements/1.1/>" + "PREFIX project:<http://www.semanticweb.org/joao/ontologies/2013/10/POIs#> ";


    private Model m_model;
    private String[] separate;
    //private ArrayList<Counted> counts;
    ArrayList<String> palavras = new ArrayList<String>();
    ArrayList<Integer> means = new ArrayList<Integer>();
    ArrayList<String> names = new ArrayList<String>();
    //ArrayList<Return> resultsList = new ArrayList<Return>();
    String userQuestion = "";
    int foundPrint = 0;

public QuerySemanticProcessor(Model m_model, String query){
        this.m_model = m_model;
        separate = query.split(" ");

    }

    public ArrayList<String> parse(String query){
        ArrayList<String> keywords = identifyKeywords();
        removeUnidentified(keywords);

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


        return keywords;
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
                QueryExecution qexec = QueryExecutionFactory.create(query, m_model);
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
}
