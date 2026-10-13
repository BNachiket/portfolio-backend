package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Collection "education": one document per qualification. Lower "position" is shown first. */
@Document(collection = "education")
public record EducationDoc(
        @Id String id,
        String degree,
        String institution,
        String year,
        String score,
        Integer position) {
}
