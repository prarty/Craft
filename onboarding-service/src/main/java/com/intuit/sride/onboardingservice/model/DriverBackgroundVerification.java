package com.intuit.sride.onboardingservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "driver_background_verifications")
@Data
public class DriverBackgroundVerification {

    @Id
    private String id;
    private String driverId;
    private boolean aadhaarVerified;
    private boolean licenseVerified;
    private boolean vehicleRCVerified;
    private Date verificationDate;
}