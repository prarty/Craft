package com.intuit.sride.onboardingservice.repository;

import com.intuit.sride.onboardingservice.model.DriverBackgroundVerification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DriverBackgroundVerificationRepository extends MongoRepository<DriverBackgroundVerification, String> {

    Optional<DriverBackgroundVerification> findByDriverId(String driverId);
}