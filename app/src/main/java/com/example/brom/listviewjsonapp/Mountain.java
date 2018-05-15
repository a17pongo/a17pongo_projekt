package com.example.brom.listviewjsonapp;

public class Mountain {
    private int id;
    private String name;
    private String type;
    private String company;
    private String location;
    private String category;
    private int size;
    private int cost;
    private String imgUrl;
    private String infoUrl;

    public Mountain(String inName, String inType,String inLocation, int inId, String inCompany,String inCategory,int inSize,int inCost, String inImgurl, String inInfoUrl){
        id=inId;
        name=inName;
        type=inType;
        company=inCompany;
        location=inLocation;
        category=inCategory;
        size=inSize;
        cost=inCost;
        imgUrl=inImgurl;
        infoUrl=inInfoUrl;
    }

    @Override
    public String toString() {
        return name;
    }



    public String info(){
        String str= "Name: " + name;
        str += "\n" + "Id: " + id;
        str += "\n" + "Type: " + type;
        str += "\n" +"Location: ";
        str += location;
        return str;
    }

    public String nameInfo() {
        return name;
    }

    public String locationInfo() {
        return location;
    }

    public String heightInfo() {
        return "" + Integer.toString(size) + " m";
    }

    public String imageUrl() {
        return imgUrl;
    }
}
