package com.btm.pagodirecto.dto;

import android.text.BoringLayout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edwinalvarado on 11/7/17.
 */

public class Commerce {

    String _id;
    String name;
    String photo_url;
    String photo_url_large;
    @SerializedName("users")
    @Expose
    String title;
    @SerializedName("short_description")
    @Expose
    String description;
    String status;
    String distance;
    Boolean favorite;

    public String getPhoto_url_large() {
        return photo_url_large;
    }

    public void setPhoto_url_large(String photo_url_large) {
        this.photo_url_large = photo_url_large;
    }

    public Commerce(String _id, String name, String photo_url, String title, String description, String status, String distance, Boolean favorite) {
        this._id = _id;
        this.name = name;
        this.photo_url = photo_url;
        this.title = title;
        this.description = description;
        this.status = status;
        this.distance = distance;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        if(distance==null) return "";
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Boolean getFavorite() {
        if(favorite==null) return false;
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

}
