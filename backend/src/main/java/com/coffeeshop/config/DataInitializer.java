package com.coffeeshop.config;

import com.coffeeshop.model.*;
import com.coffeeshop.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final MenuItemRepository menuItemRepository;
    private final GalleryItemRepository galleryItemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MenuItemRepository menuItemRepository,
                           GalleryItemRepository galleryItemRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.menuItemRepository = menuItemRepository;
        this.galleryItemRepository = galleryItemRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedMenuItems();
        seedGalleryItems();
    }

    private void seedAdminUser() {
        if (userRepository.existsByUsername("admin")) {
            logger.info("Admin user already exists, skipping creation");
            return;
        }

        // Resolve admin password from environment or generate one
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        if (adminPassword == null || adminPassword.isBlank()) {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[12];
            random.nextBytes(bytes);
            adminPassword = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            logger.warn("==============================================");
            logger.warn("GENERATED ADMIN PASSWORD: {}", adminPassword);
            logger.warn("Set ADMIN_PASSWORD environment variable for production!");
            logger.warn("==============================================");
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@coffeeshop.com");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        logger.info("Admin user created successfully");
    }

    private void seedMenuItems() {
        if (menuItemRepository.count() > 0) {
            logger.info("Menu items already exist, skipping seeding");
            return;
        }

        menuItemRepository.save(new MenuItem(
                "Hot Beverages",
                "Wide range of Steaming hot coffee to make you fresh and light.",
                new BigDecimal("350.00"),
                "Beverages",
                "image/hot-beverages.png"
        ));

        menuItemRepository.save(new MenuItem(
                "Cold Beverages",
                "Creamy and frothy cold coffee to make you cool.",
                new BigDecimal("400.00"),
                "Beverages",
                "image/cold-beverages.png"
        ));

        menuItemRepository.save(new MenuItem(
                "Refreshment",
                "Fruit and icy refreshing drink to make you feel refresh.",
                new BigDecimal("300.00"),
                "Beverages",
                "image/refreshment.png"
        ));

        menuItemRepository.save(new MenuItem(
                "Special Combo",
                "Your favourite eating and drinking combination.",
                new BigDecimal("750.00"),
                "Combo",
                "image/special-combo.png"
        ));

        menuItemRepository.save(new MenuItem(
                "Desserts",
                "Satiate your plate and take you on a culinary treat.",
                new BigDecimal("450.00"),
                "Food",
                "image/desserts.png"
        ));

        menuItemRepository.save(new MenuItem(
                "Burger & French Fries",
                "Quick bites to satisfy your small size hunger.",
                new BigDecimal("550.00"),
                "Food",
                "image/burger-frenchfries.png"
        ));

        logger.info("Seeded 6 menu items from the existing frontend");
    }

    private void seedGalleryItems() {
        if (galleryItemRepository.count() > 0) {
            logger.info("Gallery items already exist, skipping seeding");
            return;
        }

        for (int i = 1; i <= 6; i++) {
            galleryItemRepository.save(new GalleryItem(
                    "image/gallery-" + i + ".jpg",
                    "Gallery Image " + i,
                    i
            ));
        }

        logger.info("Seeded 6 gallery items from the existing frontend");
    }
}
