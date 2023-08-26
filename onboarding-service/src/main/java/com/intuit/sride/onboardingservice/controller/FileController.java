package com.intuit.sride.onboardingservice.controller;

import com.intuit.sride.onboardingservice.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/driver1")
public class FileController {

    private final FileUploadService fileUploadService;

    public FileController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/{id}/document")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        String fileId = fileUploadService.uploadFile(file, id);
        return new ResponseEntity<>("File uploaded with ID: " + fileId, HttpStatus.OK);
    }
}