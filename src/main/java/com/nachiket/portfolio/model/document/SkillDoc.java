package com.nachiket.portfolio.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/** Collection "skills": one document per skill category. Lower "position" is shown first. */
@Document(collection = "skills")
public record SkillDoc(@Id String id, String category, List<String> items, Integer position) {
}
