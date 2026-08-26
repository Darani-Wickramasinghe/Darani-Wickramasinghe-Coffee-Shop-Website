package com.coffeeshop.service;

import com.coffeeshop.dto.ContactRequest;
import com.coffeeshop.exception.ResourceNotFoundException;
import com.coffeeshop.model.ContactMessage;
import com.coffeeshop.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    public ContactMessage submitContactForm(ContactRequest request) {
        ContactMessage message = new ContactMessage();
        message.setName(request.getName());
        message.setEmail(request.getEmail());
        message.setMessage(request.getMessage());
        return contactMessageRepository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ContactMessage> getUnreadMessages() {
        return contactMessageRepository.findByReadFalseOrderByCreatedAtDesc();
    }

    public ContactMessage markAsRead(Long id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", "id", id));
        message.setRead(true);
        return contactMessageRepository.save(message);
    }
}
