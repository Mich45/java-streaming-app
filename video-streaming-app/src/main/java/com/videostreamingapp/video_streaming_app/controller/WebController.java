package com.videostreamingapp.video_streaming_app.controller;

import com.videostreamingapp.video_streaming_app.service.CloudinaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final CloudinaryService cloudinaryService;

    public WebController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/")
    public String index() {
        return "index"; // Renders index.html
    }

    @GetMapping("/player")
    public String videoPlayer(@RequestParam(name = "publicId", required = false) String publicId, Model model) {
        if (publicId != null && !publicId.isEmpty()) {
            // Generate an Streaming URL for playback in the browser
            String videoUrl = cloudinaryService.generateHlsStreamingUrlWithAutoProfile(publicId);
            model.addAttribute("videoUrl", videoUrl);
            model.addAttribute("publicId", publicId);
        } else {
            model.addAttribute("videoUrl", null);
            model.addAttribute("publicId", "No video selected.");
        }
        return "player"; // Renders player.html
    }
}