package com.nachiket.portfolio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "contact_messages")
public class ContactMessage {

    @Id
    private String id;
    private String name;
    private String email;
    private String message;
    private Instant createdAt;

    public ContactMessage() {}

    public ContactMessage(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}
