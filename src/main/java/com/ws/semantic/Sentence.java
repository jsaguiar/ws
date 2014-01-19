package com.ws.semantic;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 14/01/14
 * Time: 07:51
 * To change this template use File | Settings | File Templates.
 */
public class Sentence {

    public String sentence;
    public ArrayList<String> classes;
    public ArrayList<String> properties;
    public ArrayList<String> instances;
    public ArrayList<Triples> triples;

    public Sentence(String sentence) {
        this.sentence = sentence;
        this.classes = new ArrayList<String>();
        this.properties = new ArrayList<String>();
        this.instances = new ArrayList<String>();
    }
}
