package com.susa.ajayioluwatobi.susa;

/**
 * Created by wjl13c on 4/12/18.
 */

public class Notification {

	public String notifyID;
    public String action;
	public String timeOfPost;
	
    public String getNotifyID() {
        return notifyID;
    }


    public void setNotifyID(String notifyID) {
        this.notifyID = notifyID;
    }

	public String getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(String timeOfPost) {
        this.timeOfPost = timeOfPost;
    }
	

    public void setAction(String desc){this.action = action;}
    public String getAction(){return action;}

    public Notification(String action, String notifyID, String timeOfPost) {
        this.action = action;
        this.notifyID = notifyID;
        this.timeOfPost= timeOfPost;
    }

    public Notification() {


    }
}