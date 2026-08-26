package com.coffeeshop.controller;

import com.coffeeshop.dto.ContactRequest;
import com.coffeeshop.model.ContactMessage;
import com.coffeeshop.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> submitContactForm(
            @Valid @RequestBody ContactRequest request) {
        contactService.submitContactForm(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Thank you! Your message has been received."));
    }

    @GetMapping("/messages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        return ResponseEntity.ok(contactService.getAllMessages());
    }

    @GetMapping("/messages/unread")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessage>> getUnreadMessages() {
        return ResponseEntity.ok(contactService.getUnreadMessages());
    }

    @PutMapping("/messages/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactMessage> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.markAsRead(id));
    }
}
