package com.coffeeshop.controller;

import com.coffeeshop.dto.GalleryItemRequest;
import com.coffeeshop.model.GalleryItem;
import com.coffeeshop.service.GalleryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping
    public ResponseEntity<List<GalleryItem>> getAllGalleryItems() {
        return ResponseEntity.ok(galleryService.getAllGalleryItems());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GalleryItem> createGalleryItem(
            @Valid @RequestBody GalleryItemRequest request) {
        GalleryItem created = galleryService.createGalleryItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GalleryItem> updateGalleryItem(@PathVariable Long id,
                                                          @Valid @RequestBody GalleryItemRequest request) {
        return ResponseEntity.ok(galleryService.updateGalleryItem(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGalleryItem(@PathVariable Long id) {
        galleryService.deleteGalleryItem(id);
        return ResponseEntity.noContent().build();
    }
}
