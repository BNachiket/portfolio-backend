package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.ExperienceDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExperienceRepository extends MongoRepository<ExperienceDoc, String> {
    List<ExperienceDoc> findAllByOrderByPositionAsc();
}
