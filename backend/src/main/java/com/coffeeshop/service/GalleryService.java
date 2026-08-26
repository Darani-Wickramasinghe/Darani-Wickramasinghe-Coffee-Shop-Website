package com.coffeeshop.service;

import com.coffeeshop.dto.GalleryItemRequest;
import com.coffeeshop.exception.ResourceNotFoundException;
import com.coffeeshop.model.GalleryItem;
import com.coffeeshop.repository.GalleryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryService {

    private final GalleryItemRepository galleryItemRepository;

    public GalleryService(GalleryItemRepository galleryItemRepository) {
        this.galleryItemRepository = galleryItemRepository;
    }

    public List<GalleryItem> getAllGalleryItems() {
        return galleryItemRepository.findAllByOrderByDisplayOrderAsc();
    }

    public GalleryItem getGalleryItemById(Long id) {
        return galleryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GalleryItem", "id", id));
    }

    public GalleryItem createGalleryItem(GalleryItemRequest request) {
        GalleryItem item = new GalleryItem();
        item.setImageUrl(request.getImageUrl());
        item.setCaption(request.getCaption());
        item.setDisplayOrder(request.getDisplayOrder());
        return galleryItemRepository.save(item);
    }

    public GalleryItem updateGalleryItem(Long id, GalleryItemRequest request) {
        GalleryItem item = getGalleryItemById(id);
        item.setImageUrl(request.getImageUrl());
        item.setCaption(request.getCaption());
        item.setDisplayOrder(request.getDisplayOrder());
        return galleryItemRepository.save(item);
    }

    public void deleteGalleryItem(Long id) {
        GalleryItem item = getGalleryItemById(id);
        galleryItemRepository.delete(item);
    }
}
