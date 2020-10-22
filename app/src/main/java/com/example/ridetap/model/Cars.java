package com.example.ridetap.model;

import java.util.List;

public class Cars {
    private String ImageURL;
    private String Model;
    private String Location;
    private String Price;
    private String Contact;
    private List<Cars> carsList;
//    private List<Cars> carsListFiltered;


    public Cars() {
    }

    public Cars(String imageURL, String model, String location, String price, String contact) {
        ImageURL = imageURL;
        Model = model;
        Location = location;
        Price = price;
        Contact = contact;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public List<Cars> getCarsList() {
        return carsList;
    }

    public void setCarsList(List<Cars> carsList) {
        this.carsList = carsList;
    }

//    public List<Cars> getCarsListFiltered() {
//        return carsListFiltered;
//    }
//
//    public void setCarsListFiltered(List<Cars> carsListFiltered) {
//        this.carsListFiltered = carsListFiltered;
//    }
}
