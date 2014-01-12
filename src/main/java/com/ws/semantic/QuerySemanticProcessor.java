package com.ws.semantic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 12/01/14
 * Time: 01:21
 * To change this template use File | Settings | File Templates.
 */
public class QuerySemanticProcessor {
    public static ArrayList<String> parse(String query){
        ArrayList<String> keywords = identifyKeywords();
        removeUnidentified(keywords);
        return keywords;
    }
    public static ArrayList<String> identifyKeywords(){
        return new ArrayList<String>();
    }
    public static ArrayList<String> removeUnidentified(ArrayList<String> keywords){
        return keywords;
    }
}
