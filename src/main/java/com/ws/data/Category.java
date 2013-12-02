package com.ws.data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 02/12/13
 * Time: 00:03
 * To change this template use File | Settings | File Templates.
 */
public class Category {
    String id;
    String name;
    String pluralName;
    String shortName;
    String icon;
    List parents;

    public Category(String id, String name, String pluralName, String shortName, String icon, List parents) {
        this.id = id;
        this.name = name;
        this.pluralName = pluralName;
        this.shortName = shortName;
        this.icon = icon;
        this.parents = parents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List getParents() {
        return parents;
    }

    public void setParents(List parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pluralName='" + pluralName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", icon='" + icon + '\'' +
                ", parents=" + parents +
                '}';
    }
}
