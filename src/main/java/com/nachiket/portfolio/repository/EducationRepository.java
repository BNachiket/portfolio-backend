package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.EducationDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EducationRepository extends MongoRepository<EducationDoc, String> {
    List<EducationDoc> findAllByOrderByPositionAsc();
}
