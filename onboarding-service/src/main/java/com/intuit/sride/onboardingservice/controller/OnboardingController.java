package com.intuit.sride.onboardingservice.controller;

import com.intuit.sride.onboardingservice.service.OnboardingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/driver")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("{id}/documents")
    public ResponseEntity<String> collectDocuments(@PathVariable("id") String id, @RequestParam("files") List<MultipartFile> files) {
        onboardingService.uploadFilesForDriver(id, files);
        return new ResponseEntity<>("Document collection process started.", HttpStatus.OK);
    }

    @PostMapping("{id}/background-verification")
    public ResponseEntity<String> performBackgroundVerification(@PathVariable String id) {
        onboardingService.performBackgroundVerification(id);
        return new ResponseEntity<>("Background verification process started.", HttpStatus.OK);
    }

    @PostMapping("{id}/ship-device")
    public ResponseEntity<String> shipTrackingDevice(@PathVariable String id) {
        onboardingService.shipTrackingDevice(id);
        return new ResponseEntity<>("Shipping tracking device process started.", HttpStatus.OK);
    }
}