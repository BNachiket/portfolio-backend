package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/** Collection "experience": one document per job. Lower "position" is shown first. */
@Document(collection = "experience")
public record ExperienceDoc(
        @Id String id,
        String role,
        String company,
        String period,
        List<String> bullets,
        Integer position) {
}
