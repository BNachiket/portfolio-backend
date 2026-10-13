package com.nachiket.portfolio.service;

import com.nachiket.portfolio.model.ContactMessage;
import com.nachiket.portfolio.model.ContactRequest;
import com.nachiket.portfolio.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    public ContactMessage save(ContactRequest request) {
        return repository.save(new ContactMessage(
                request.name().trim(),
                request.email().trim(),
                request.message().trim()));
    }
}
