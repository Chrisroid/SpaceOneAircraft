package com.example.spaceoneaircraft;

import com.google.gson.annotations.SerializedName;

public class Aircrafts {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("manufacturingYear")
    private String manufacturingYear;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(String manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }
}
