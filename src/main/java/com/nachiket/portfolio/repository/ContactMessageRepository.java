package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.ContactMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactMessageRepository extends MongoRepository<ContactMessage, String> {
}
