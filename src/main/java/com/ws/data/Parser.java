package com.ws.data;

import com.hp.hpl.jena.rdf.model.Model;

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

    public Parser(){
        points_of_interest = new JsonParser().points_of_interest;




    }

}
