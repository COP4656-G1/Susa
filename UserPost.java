package com.susa.ajayioluwatobi.susa;

/**
 * Created by ajayioluwatobi on 4/9/18.
 */

public class UserPost {

    public int price;
	public String userID;
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
	
	public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public UserPost(int price, String address, String image, String location,String desc, String userID, String timeOf) {
        this.price = price;
        this.address = address;
        this.image= image;
        this.location= location;
        this.desc = desc;
		this.userID = userID;
		this.timeOf = timeOf;
    }

    public UserPost() {


    }
}
