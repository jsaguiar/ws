package com.ws.semantic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 12/01/14
 * Time: 01:15
 * To change this template use File | Settings | File Templates.
 */
public class Search {
    ArrayList<String> keywords;

    public Search(String query) {
        keywords = QuerySemanticProcessor.parse(query);
    }
}
