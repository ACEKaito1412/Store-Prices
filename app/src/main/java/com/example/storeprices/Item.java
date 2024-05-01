package com.example.storeprices;

public class Item {
    String name;
    String code;
    String price;

    String date;



    public Item(String name, String code, String price, String date) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

