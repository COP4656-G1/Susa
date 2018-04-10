package com.susa.ajayioluwatobi.susa;

/**
 * Created by ajayioluwatobi on 4/9/18.
 */

public class UserPost {

    public int price;
    public String address;
    public String image;
    public String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserPost(int price, String address, String image, String location) {
        this.price = price;
        this.address = address;
        this.image= image;
        this.location= location;
    }

    public UserPost() {


    }
}
