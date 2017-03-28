package com.example.aharon.testapp;

/**
 * Created by User on 28/03/2017.
 */

public class Expence {
    String name,desc;
    double price;

    public Expence(){}

    public Expence(String name, String desc, double price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
