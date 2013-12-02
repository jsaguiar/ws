package com.ws.data;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 27/11/13
 * Time: 00:33
 * To change this template use File | Settings | File Templates.
 */

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.ws.extremespots.StaticVariables;

public class JsonParser {
    ArrayList<PointOfInterest> points_of_interest;

    public JsonParser(){
        try {
            //read file
            BufferedReader br = new BufferedReader(new FileReader(StaticVariables.points_of_interest_json_file));
            String line;
            points_of_interest = new ArrayList<PointOfInterest>();
            //for each line
            PointOfInterest poi = new PointOfInterest();
            while ((line = br.readLine()) != null) {
                //parse point of interest
                poi = parsePointOfInterest(line);
                addPointOfInterest(poi);
            }
            br.close();
            //System.out.println("Printing the last poi parsed:");
            System.out.println(poi.toString());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private PointOfInterest parsePointOfInterest(String pointOfInterest){
        Gson gson = new Gson();
        PointOfInterest poi = gson.fromJson(pointOfInterest.toString(), PointOfInterest.class);
        return poi;
    }

    private void addPointOfInterest(PointOfInterest poi){
        if(poi.categories.size()>0){
            points_of_interest.add(poi);
        }

    }
    public static void main (String args[]) {
        JsonParser jp = new JsonParser();
        //System.out.println(jp.points_of_interest.get(0).toString());


    }



}
