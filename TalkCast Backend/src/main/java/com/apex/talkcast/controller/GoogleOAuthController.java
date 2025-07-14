package com.apex.talkcast.controller;

import com.apex.talkcast.service.YouTubeBroadcastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GoogleOAuthController {

    private final YouTubeBroadcastService googleOAuthService;

    @PostMapping("/api/youtube/broadcast")
    public ResponseEntity<?> createBroadcast(@RequestBody Map<String, String> request) {
    try {
        String code = request.get("token");
      return ResponseEntity.ok(googleOAuthService.createBroadcast(code));
    } catch (Exception e) {
      log.error("Failed to create YouTube broadcast", e);
      return ResponseEntity.status(500).body("Broadcast creation failed: " + e.getMessage());
    }
  }
}
