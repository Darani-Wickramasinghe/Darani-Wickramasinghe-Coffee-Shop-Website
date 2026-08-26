package com.coffeeshop.repository;

import com.coffeeshop.model.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    List<GalleryItem> findAllByOrderByDisplayOrderAsc();
}
