package com.example.nikhil.events;

public class Event {
    private String title,desc,image,contact_info,reg_fee,venue,branch,postId;
    public Event(){

    }
    public Event(String title, String desc, String image, String contact_info, String reg_fee, String venue, String branch,String id) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.contact_info = contact_info;
        this.reg_fee = reg_fee;
        this.venue = venue;
        this.branch = branch;
        this.postId =id;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReg_fee() {
        return reg_fee;
    }

    public void setReg_fee(String reg_fee) {
        this.reg_fee = reg_fee;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
