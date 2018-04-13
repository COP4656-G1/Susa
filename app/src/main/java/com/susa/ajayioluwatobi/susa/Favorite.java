package com.susa.ajayioluwatobi.susa;

/**
 * Created by wjl13c on 4/12/18.
 */

public class Favorite {

    public int price;
	public String userLikedID;
    public String address;
    public String image;
    public String location;
    public String desc;
	public String timeOf;
	
    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }
	


    public int getPrice() {
        return price;
    }
	
	public String getUserLikedID() {
        return userLikedID;
    }

    public void setUserLikedID(String userLikedID) {
        this.userLikedID = userLikedID;
    }
	public String getTimeOf() {
        return timeOf;
    }

    public void setTimeOf(String timeOf) {
        this.timeOf = timeOf;
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

    public void setDesc(String desc){this.desc = desc;}
    public String getDesc(){return desc;}

    public Favorite(int price, String address, String image, String location,String desc, String userLikedID, String timeOf) {
        this.price = price;
        this.address = address;
        this.image= image;
        this.location= location;
        this.desc = desc;
		this.userLikedID = userLikedID;
		this.timeOf = timeOf;
    }

    public Favorite() {


    }
}