package com.ws.semantic;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 22/01/14
 * Time: 03:19
 * To change this template use File | Settings | File Templates.
 */
public class MapPoint {

    public String id;
    public String name;
    public String lat;
    public String lng;
    public String description;
    public String price;
    public String address;
    public String rating;

    public MapPoint(String id, String name, String lat, String lng, String description, String price, String address, String rating) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.price = price;
        this.address = address;
        this.rating = rating;
    }
}
