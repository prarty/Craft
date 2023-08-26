package com.intuit.sride.onboardingservice.repository;

import com.intuit.sride.onboardingservice.model.DriverDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DriverDocumentRepository extends MongoRepository<DriverDocument, String> {

    Optional<DriverDocument> findByDriverId(String userId);
}