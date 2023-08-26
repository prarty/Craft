package com.intuit.sride.onboardingservice.service;

import com.intuit.sride.onboardingservice.model.DriverBackgroundVerification;
import com.intuit.sride.onboardingservice.repository.DriverBackgroundVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DriverBackgroundVerificationService {

    private final DriverBackgroundVerificationRepository verificationRepository;

    public DriverBackgroundVerificationService(DriverBackgroundVerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    public void performBackgroundVerification(String driverId, boolean aadhaarVerified, boolean licenseVerified, boolean vehicleRCVerified) {
        Optional<DriverBackgroundVerification> optionalVerification = verificationRepository.findByDriverId(driverId);
        DriverBackgroundVerification verificationRecord = optionalVerification.orElseGet(DriverBackgroundVerification::new);
        
        verificationRecord.setDriverId(driverId);
        verificationRecord.setAadhaarVerified(aadhaarVerified);
        verificationRecord.setLicenseVerified(licenseVerified);
        verificationRecord.setVehicleRCVerified(vehicleRCVerified);
        verificationRecord.setVerificationDate(new Date());

        verificationRepository.save(verificationRecord);
    }

    public Optional<DriverBackgroundVerification> getBackgroundVerificationForDriver(String driverId) {
        return verificationRepository.findByDriverId(driverId);
    }
}