package com.apex.talkcast.controller;

import com.apex.talkcast.request.BroadcastReq;
import com.apex.talkcast.request.YoutubeVideoUploadReq;
import com.apex.talkcast.service.S3Service;
import com.apex.talkcast.service.youtube.YoutubeService;
import com.apex.talkcast.service.youtube.YoutubeUploadService;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/youtube")
public class YoutubeController {

    private final YoutubeService youtubeService;
    private final YoutubeUploadService youtubeUploadService;
    private final S3Service s3Service;

    @PostMapping("/broadcast")
    public ResponseEntity<String> createBroadcast(@ModelAttribute BroadcastReq request,
                                                  HttpServletRequest httpRequest) {
        try {
            YouTube youtubeClient = (YouTube) httpRequest.getAttribute("youtubeClient");
            if (youtubeClient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("YouTube client not found. Authorization token missing or invalid.");
            }

            String response = youtubeService.scheduleStream(request, youtubeClient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create YouTube broadcast", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Broadcast creation failed: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload-thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3Service.uploadThumbnail(file);
            return ResponseEntity.ok().body(Map.of("url", imageUrl));
        } catch (Exception e) {
            log.error("Failed to upload thumbnail", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/upload-video")
    public CompletableFuture<Video> uploadYoutubeVideo(
            @RequestPart("file") MultipartFile file, @RequestPart("metadata") YoutubeVideoUploadReq metadata,
            HttpServletRequest httpRequest) throws IOException {
        YouTube youtubeClient = (YouTube) httpRequest.getAttribute("youtubeClient");
        return youtubeUploadService.uploadVideoAsync(
                metadata,
                file.getInputStream(),
                file.getContentType(),
                file.getSize(),
                youtubeClient // Your injected YouTube client
        );
    }

    @GetMapping("/playlists")
    public ResponseEntity<?> getPlaylists(HttpServletRequest httpRequest, String channelId) {
        YouTube youtubeClient = (YouTube) httpRequest.getAttribute("youtubeClient");
        try {
            return ResponseEntity.ok(youtubeService.getPlaylists(youtubeClient));
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
