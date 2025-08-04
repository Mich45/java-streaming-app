package com.videostreamingapp.video_streaming_app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoStreamingAppApplication {

    public static void main(String[] args) {
        // Load .env file at application startup
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(VideoStreamingAppApplication.class, args);
    }
}