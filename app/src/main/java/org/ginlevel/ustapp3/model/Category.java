package org.ginlevel.ustapp3.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Category {

    private String name;
    private String imageUrl;

    public Category(){
    }

    public Category(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
