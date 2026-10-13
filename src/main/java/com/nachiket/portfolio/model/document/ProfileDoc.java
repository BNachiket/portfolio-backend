package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Collection "profile": one document with _id "main". */
@Document(collection = "profile")
public record ProfileDoc(
        @Id String id,
        String name,
        String title,
        String email,
        String phone,
        String linkedin,
        String photo,
        String summary) {

    public static final String MAIN_ID = "main";
}
