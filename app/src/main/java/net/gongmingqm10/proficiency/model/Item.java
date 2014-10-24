package net.gongmingqm10.proficiency.model;

import java.io.Serializable;

public class Item implements Serializable {

    private String title;
    private String description;
    private String imageHref;

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    public boolean isValid() {
        return title != null || description != null || imageHref != null;
    }
}
