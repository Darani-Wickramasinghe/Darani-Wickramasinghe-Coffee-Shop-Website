package com.coffeeshop.dto;

import jakarta.validation.constraints.Size;

public class GalleryItemRequest {

    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String imageUrl;

    @Size(max = 200, message = "Caption must not exceed 200 characters")
    private String caption;

    private int displayOrder;

    public GalleryItemRequest() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
