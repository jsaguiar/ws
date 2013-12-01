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

public class JsonParser {

    public JsonParser(){
        try {
            //read file
            BufferedReader br = new BufferedReader(new FileReader("data/point_of_interests.json"));
            String line;
            //for each line
            System.out.println("Start");
            PointOfInterest poi = new PointOfInterest();
            while ((line = br.readLine()) != null) {
                //parse point of interest
                poi = parsePointOfInterest(line);
            }
            br.close();
            System.out.println("Printing the last poi parsed:");
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

    public static void main (String args[]) {
        JsonParser jp = new JsonParser();
    }

}
