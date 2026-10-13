package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.SkillDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SkillRepository extends MongoRepository<SkillDoc, String> {
    List<SkillDoc> findAllByOrderByPositionAsc();
}
