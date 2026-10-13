package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Collection "certifications": one document per certification. */
@Document(collection = "certifications")
public record CertificationDoc(@Id String id, String title, Integer position) {
}
