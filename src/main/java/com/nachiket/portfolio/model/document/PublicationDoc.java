package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Collection "publications": one document per publication citation. */
@Document(collection = "publications")
public record PublicationDoc(@Id String id, String citation, Integer position) {
}
