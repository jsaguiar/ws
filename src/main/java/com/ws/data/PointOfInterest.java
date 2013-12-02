package com.ws.data;

import java.util.List;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 27/11/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class PointOfInterest {

    String city;
    String country;
    Boolean gmaps;
    String name;
    String id;

    Contact contact;
    HashMap location;
    List<Category> categories;
    Boolean verified;

    Boolean restricted;
    HashMap stats;
    String url;
    HashMap likes;
    String canonicalUrl;

    List specials;
    HashMap photos;
    HashMap hereNow;
    String description;
    Integer createdAt;

    HashMap mayor;
    HashMap tips;
    List tags;
    String shortUrl;
    String timeZone;

    HashMap listed;
    HashMap pageUpdates;
    List groups;
    Float rating;
    HashMap hours;

    HashMap popular;
    HashMap menu;
    HashMap price;
    String storeId;
    List specialsNearby;

    HashMap beenHere;
    Boolean like;
    Boolean dislike;
    List roles;

    HashMap flags;
    HashMap page;
    Integer current_status;

    public PointOfInterest(){

    }

    public PointOfInterest(String city, String country, Boolean gmaps, String name, String id, Contact contact, HashMap location, List categories, Boolean verified, Boolean restricted, HashMap stats, String url, HashMap likes, String canonicalUrl, List specials, HashMap photos, HashMap hereNow, String description, Integer createdAt, HashMap mayor, HashMap tips, List tags, String shortUrl, String timeZone, HashMap listed, HashMap pageUpdates, List groups, Float rating, HashMap hours, HashMap popular, HashMap menu, HashMap price, String storeId, List specialsNearby, HashMap beenHere, Boolean like, Boolean dislike, List roles, HashMap flags, HashMap page, Integer current_status) {
        this.city = city;
        this.country = country;
        this.gmaps = gmaps;
        this.name = name;
        this.id = id;
        this.contact = contact;
        this.location = location;
        this.categories = categories;
        this.verified = verified;
        this.restricted = restricted;
        this.stats = stats;
        this.url = url;
        this.likes = likes;
        this.canonicalUrl = canonicalUrl;
        this.specials = specials;
        this.photos = photos;
        this.hereNow = hereNow;
        this.description = description;
        this.createdAt = createdAt;
        this.mayor = mayor;
        this.tips = tips;
        this.tags = tags;
        this.shortUrl = shortUrl;
        this.timeZone = timeZone;
        this.listed = listed;
        this.pageUpdates = pageUpdates;
        this.groups = groups;
        this.rating = rating;
        this.hours = hours;
        this.popular = popular;
        this.menu = menu;
        this.price = price;
        this.storeId = storeId;
        this.specialsNearby = specialsNearby;
        this.beenHere = beenHere;
        this.like = like;
        this.dislike = dislike;
        this.roles = roles;
        this.flags = flags;
        this.page = page;
        this.current_status = current_status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getGmaps() {
        return gmaps;
    }

    public void setGmaps(Boolean gmaps) {
        this.gmaps = gmaps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public HashMap getLocation() {
        return location;
    }

    public void setLocation(HashMap location) {
        this.location = location;
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public HashMap getStats() {
        return stats;
    }

    public void setStats(HashMap stats) {
        this.stats = stats;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap getLikes() {
        return likes;
    }

    public void setLikes(HashMap likes) {
        this.likes = likes;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public List getSpecials() {
        return specials;
    }

    public void setSpecials(List specials) {
        this.specials = specials;
    }

    public HashMap getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap photos) {
        this.photos = photos;
    }

    public HashMap getHereNow() {
        return hereNow;
    }

    public void setHereNow(HashMap hereNow) {
        this.hereNow = hereNow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public HashMap getMayor() {
        return mayor;
    }

    public void setMayor(HashMap mayor) {
        this.mayor = mayor;
    }

    public HashMap getTips() {
        return tips;
    }

    public void setTips(HashMap tips) {
        this.tips = tips;
    }

    public List getTags() {
        return tags;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public HashMap getListed() {
        return listed;
    }

    public void setListed(HashMap listed) {
        this.listed = listed;
    }

    public HashMap getPageUpdates() {
        return pageUpdates;
    }

    public void setPageUpdates(HashMap pageUpdates) {
        this.pageUpdates = pageUpdates;
    }

    public List getGroups() {
        return groups;
    }

    public void setGroups(List groups) {
        this.groups = groups;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public HashMap getHours() {
        return hours;
    }

    public void setHours(HashMap hours) {
        this.hours = hours;
    }

    public HashMap getPopular() {
        return popular;
    }

    public void setPopular(HashMap popular) {
        this.popular = popular;
    }

    public HashMap getMenu() {
        return menu;
    }

    public void setMenu(HashMap menu) {
        this.menu = menu;
    }

    public HashMap getPrice() {
        return price;
    }

    public void setPrice(HashMap price) {
        this.price = price;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List getSpecialsNearby() {
        return specialsNearby;
    }

    public void setSpecialsNearby(List specialsNearby) {
        this.specialsNearby = specialsNearby;
    }

    public HashMap getBeenHere() {
        return beenHere;
    }

    public void setBeenHere(HashMap beenHere) {
        this.beenHere = beenHere;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean getDislike() {
        return dislike;
    }

    public void setDislike(Boolean dislike) {
        this.dislike = dislike;
    }

    public List getRoles() {
        return roles;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public HashMap getFlags() {
        return flags;
    }

    public void setFlags(HashMap flags) {
        this.flags = flags;
    }

    public HashMap getPage() {
        return page;
    }

    public void setPage(HashMap page) {
        this.page = page;
    }

    public Integer getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(Integer current_status) {
        this.current_status = current_status;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", gmaps=" + gmaps +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", contact=" + contact +
                ", location=" + location +
                ", categories=" + categories +
                ", verified=" + verified +
                ", restricted=" + restricted +
                ", stats=" + stats +
                ", url='" + url + '\'' +
                ", likes=" + likes +
                ", canonicalUrl='" + canonicalUrl + '\'' +
                ", specials=" + specials +
                ", photos=" + photos +
                ", hereNow=" + hereNow +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", mayor=" + mayor +
                ", tips=" + tips +
                ", tags=" + tags +
                ", shortUrl='" + shortUrl + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", listed=" + listed +
                ", pageUpdates=" + pageUpdates +
                ", groups=" + groups +
                ", rating=" + rating +
                ", hours=" + hours +
                ", popular=" + popular +
                ", menu=" + menu +
                ", price=" + price +
                ", storeId='" + storeId + '\'' +
                ", specialsNearby=" + specialsNearby +
                ", beenHere=" + beenHere +
                ", like=" + like +
                ", dislike=" + dislike +
                ", roles=" + roles +
                ", flags=" + flags +
                ", page=" + page +
                ", current_status=" + current_status +
                '}';
    }
}
