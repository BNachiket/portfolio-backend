package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.PublicationDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PublicationRepository extends MongoRepository<PublicationDoc, String> {
    List<PublicationDoc> findAllByOrderByPositionAsc();
}
