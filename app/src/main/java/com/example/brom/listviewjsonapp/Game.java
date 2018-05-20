package com.example.brom.listviewjsonapp;

public class Game {
    private int id;
    private String name;
    //private String type;
    private String company;
    private String location;
    private String category;
    private int size;
    private int cost;
    private String imgUrl;
    private String infoUrl;

    public Game(String inName,  int inId, String inCompany, String inCategory, int inSize){
        id=inId;
        name=inName;
        //type=inType;
        company=inCompany;
        //location=inLocation;
        category=inCategory;
        size=inSize;
        //cost=inCost;
        //imgUrl=inImgurl;
        //infoUrl=inInfoUrl;
    }

    @Override
    public String toString() {
        return name;
    }



    public String info(){
        String str= "Name: " + name;
        str += "\n" + "Id: " + id;
        return str;
    }

    public String nameInfo() {
        return name;
    }

    public String locationInfo() {
        return location;
    }

    public String heightInfo() {
        return "" + Integer.toString(size) + " GB";
    }

    public String imageUrl() {
        return imgUrl;
    }
}
