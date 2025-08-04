package com.videostreamingapp.video_streaming_app.controller;

import com.videostreamingapp.video_streaming_app.service.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final CloudinaryService cloudinaryService;

    public VideoController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // Endpoint to handle video upload requests.
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadVideo(file);
            if (uploadResult != null && uploadResult.containsKey("secure_url")) {
                String videoUrl = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");
                return new ResponseEntity<>("Video uploaded successfully. Public ID: " + publicId + ", URL: " + videoUrl, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Video upload failed or no URL returned.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Error during file upload: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}