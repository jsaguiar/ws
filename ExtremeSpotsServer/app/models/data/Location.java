package models.data;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 02/12/13
 * Time: 01:02
 * To change this template use File | Settings | File Templates.
 */
public class Location {
    String address;
    String crossStreet;
    Double lat;
    Double lng;
    String postalCode;
    String cc;
    String city;
    String state;
    String country;

    public Location(String address, String crossStreet, Double lat, Double lng, String postalCode, String cc, String city, String state, String country) {
        this.address = address;
        this.crossStreet = crossStreet;
        this.lat = lat;
        this.lng = lng;
        this.postalCode = postalCode;
        this.cc = cc;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", crossStreet='" + crossStreet + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", postalCode='" + postalCode + '\'' +
                ", cc='" + cc + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
