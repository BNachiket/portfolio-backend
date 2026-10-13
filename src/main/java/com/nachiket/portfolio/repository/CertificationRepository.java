package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.CertificationDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CertificationRepository extends MongoRepository<CertificationDoc, String> {
    List<CertificationDoc> findAllByOrderByPositionAsc();
}
