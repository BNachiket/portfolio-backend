package com.nachiket.portfolio.repository;

import com.nachiket.portfolio.model.document.ProfileDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<ProfileDoc, String> {
}
