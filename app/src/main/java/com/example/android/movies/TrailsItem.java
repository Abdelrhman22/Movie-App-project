package com.example.android.movies;


public class TrailsItem implements ParentItemInterface {


    String ID,type,iso,key,name,site,size;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public void setId(String id) {
        this.ID=id;
    }

    @Override
    public String getId() {
        return this.ID;
    }

    public static   int TrailType=1;
    @Override
    public int getListType() {
        return TrailType;
    }

    @Override
    public void setListType(int type) {

    }
}
