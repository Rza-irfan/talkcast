package com.apex.talkcast.controller;

import com.apex.talkcast.request.BroadcastReq;
import com.apex.talkcast.service.YouTubeBroadcastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GoogleOAuthController {

    private final YouTubeBroadcastService googleOAuthService;

    @PostMapping("/api/youtube/broadcast")
    public ResponseEntity<String> createBroadcast(@RequestBody BroadcastReq request) {
    try {
        var response = googleOAuthService.createBroadcast(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Failed to create YouTube broadcast", e);
      return ResponseEntity.status(500).body("Broadcast creation failed: " + e.getMessage());
    }
  }
}
