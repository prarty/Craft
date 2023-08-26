package com.intuit.sride.onboardingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "driver_document")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDocument {

    @Id
    private String id;
    private String driverId;
    private List<UploadedFile> uploadedFiles;
}