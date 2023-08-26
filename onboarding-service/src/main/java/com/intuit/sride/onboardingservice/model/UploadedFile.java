package com.intuit.sride.onboardingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFile {
    private String fileId;
    private String fileName;
    private boolean verified;
}