package com.videostreamingapp.video_streaming_app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // Initialize cloudinary with credentials from environment variables
    public CloudinaryService(@Value("${CLOUDINARY_CLOUD_NAME}") String cloudName,
                             @Value("${CLOUDINARY_API_KEY}") String apiKey,
                             @Value("${CLOUDINARY_API_SECRET}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    // Upload video file to Cloudinary

    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadVideo(MultipartFile multipartFile) throws IOException {
        File fileToUpload = convertMultiPartToFile(multipartFile);

        try {
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(fileToUpload,
                    ObjectUtils.asMap("resource_type", "video"));
            
            return uploadResult;
        } catch (IOException e) {
            System.err.println("Cloudinary upload failed: " + e.getMessage());
            throw e; 
        } finally {
            if (fileToUpload != null && fileToUpload.exists()) {
                fileToUpload.delete();
            }
        }
    }
    // Generates an HLS streaming URL for a video using the 'auto' streaming profile.
     public String generateHlsStreamingUrlWithAutoProfile(String publicId) {
        return cloudinary.url()
                .transformation(new Transformation<>().streamingProfile("auto"))
                .resourceType("video")
                .format("m3u8")
                .generate(publicId);
    }

    // Helper method to convert MultipartFile to a temporary File
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + Objects.requireNonNull(file.getOriginalFilename());
        File convFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}