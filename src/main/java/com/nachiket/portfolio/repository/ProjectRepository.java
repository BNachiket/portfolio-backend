package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.ProjectDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<ProjectDoc, String> {
    List<ProjectDoc> findAllByOrderByPositionAsc();
}
