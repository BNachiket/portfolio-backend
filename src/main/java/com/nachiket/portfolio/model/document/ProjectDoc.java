package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/** Collection "projects": one document per project. Lower "position" is shown first. */
@Document(collection = "projects")
public record ProjectDoc(
        @Id String id,
        String name,
        String subtitle,
        List<String> tags,
        List<String> bullets,
        Integer position) {
}
