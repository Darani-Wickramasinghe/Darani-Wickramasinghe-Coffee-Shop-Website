package com.coffeeshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "gallery_items")
public class GalleryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Size(max = 200)
    private String caption;

    @Column(name = "display_order")
    private int displayOrder;

    // Constructors
    public GalleryItem() {}

    public GalleryItem(String imageUrl, String caption, int displayOrder) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.displayOrder = displayOrder;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
