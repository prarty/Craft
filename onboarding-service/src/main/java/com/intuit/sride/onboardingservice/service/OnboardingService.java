package com.intuit.sride.onboardingservice.service;

import com.intuit.sride.onboardingservice.model.DriverDocument;
import com.intuit.sride.onboardingservice.model.UploadedFile;
import com.intuit.sride.onboardingservice.repository.DriverDocumentRepository;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OnboardingService {

    final DriverDocumentRepository driverDocumentRepository;
    private final GridFsTemplate gridFsTemplate;
    final DriverBackgroundVerificationService driverBackgroundVerificationService;

    public OnboardingService(DriverDocumentRepository driverDocumentRepository, GridFsTemplate gridFsTemplate, DriverBackgroundVerificationService driverBackgroundVerificationService) {
        this.driverDocumentRepository = driverDocumentRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.driverBackgroundVerificationService = driverBackgroundVerificationService;
    }

    public void uploadFilesForDriver(String id, List<MultipartFile> files) {
        List<UploadedFile> uploadedFiles = files.stream()
                .map(this::uploadFile)
                .collect(Collectors.toList());

        updateDriverUploadedFiles(id, uploadedFiles);
    }

    private void updateDriverUploadedFiles(String driverId, List<UploadedFile> uploadedFiles) {
//        driverDocumentRepository.save()
        Optional<DriverDocument> optionalDriverDocument = driverDocumentRepository.findByDriverId(driverId);
//        if (optionalDriverDocument.isPresent()) {
            DriverDocument driverDocument = optionalDriverDocument.orElse(new DriverDocument());
            driverDocument.setUploadedFiles(uploadedFiles);
            driverDocument.setDriverId(driverId);
            driverDocumentRepository.save(driverDocument);
//        } else {

//        }
    }

    private UploadedFile uploadFile(MultipartFile file) {
        try {
            String fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType()).toString();
            return new UploadedFile(fileId, file.getOriginalFilename(), false);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    public void performBackgroundVerification(String driverId) {
        System.out.println("Performing background verification for user: " + driverId);

        DriverDocument driverDocument = driverDocumentRepository.findByDriverId(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));

        driverBackgroundVerificationService.performBackgroundVerification(driverId,
                isAadhaarFileUploaded(driverDocument),
                isDrivingLicenseUploaded(driverDocument),
                isVehicleRCFileUploaded(driverDocument));
    }

    public void shipTrackingDevice(String driverId) {
        // Logic to ship tracking device
        System.out.println("Shipping tracking device for user: " + driverId);
    }

    private boolean isAadhaarFileUploaded(DriverDocument driverDocument) {
        return isFileUploaded(driverDocument.getUploadedFiles(), ".*aadhaar.*");
    }

    private boolean isDrivingLicenseUploaded(DriverDocument driverDocument) {
        return isFileUploaded(driverDocument.getUploadedFiles(), ".*DrivingLicense.*");
    }

    private boolean isVehicleRCFileUploaded(DriverDocument driverDocument) {
        return isFileUploaded(driverDocument.getUploadedFiles(), ".*VehicleRC.*");
    }

    private boolean isFileUploaded(List<UploadedFile> uploadedFiles, String fileName) {
        return uploadedFiles.stream()
                .anyMatch(file -> fileName.equalsIgnoreCase(file.getFileName()));
    }

}