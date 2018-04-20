package com.example.brom.listviewjsonapp;

public class Mountain {
    private int id;
    private String name;
    private String type;
    private String company;
    private String location;
    private String category;
    private int seize;
    private int cost;
    private String auxData;

    public Mountain(String inName, String inType,String inLocation){
        name=inName;
        type=inType;
        location=inLocation;
    }

    public String info(){
        String str=name;
        str += " is located ";
        str += location;
        str += " av typen: ";
        str += type;
        return str;
    }
}
