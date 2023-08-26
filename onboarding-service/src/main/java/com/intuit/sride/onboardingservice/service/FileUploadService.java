package com.intuit.sride.onboardingservice.service;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileUploadService {

    private final GridFsTemplate gridFsTemplate;

    public FileUploadService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public String uploadFile(MultipartFile file, Long id) throws IOException {
        InputStream inputStream = file.getInputStream();
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        Document documentDetails = new Document();
        documentDetails.append("driverId", id);
//        documentDetails.append("username", username);

        // Upload the file to GridFS
        return gridFsTemplate.store(inputStream, fileName, contentType, new BasicDBObject(documentDetails)).toString();
    }
}