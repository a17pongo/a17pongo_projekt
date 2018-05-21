package com.example.brom.listviewjsonapp;

public class Game {
    private String name;
    private String company;
    private String category;
    private int size;
    //private String auxdata;


    public Game(String inName, String inCompany, String inCategory, int inSize){
        name=inName;
        company=inCompany;
        category=inCategory;
        size=inSize;
        //auxdata=inAuxdata;
    }

    @Override
    public String toString() {
        return name;
    }



    public String info(){
        String str= "Name: " + name;
        str += "Company: " + company;

        return str;
    }

    public String nameInfo() {
        return name;
    }

    public String companyInfo() {
        return "Company: " + company;
    }

    public String categoryInfo() {
        return "Category: " + category;
    }

    public String sizeInfo() {
        return "" + Integer.toString(size) + " GB";
    }

    /*public String gameDescInfo() {
        return auxdata;
    }*/
}
