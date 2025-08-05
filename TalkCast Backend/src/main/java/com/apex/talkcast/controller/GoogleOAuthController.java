package com.apex.talkcast.controller;

import com.apex.talkcast.response.AuthResponse;
import com.apex.talkcast.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GoogleOAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/api/access-token")
    public ResponseEntity<AuthResponse> getAccessToken(@RequestParam String authCode) {
        var response = oAuthService.getAccessToken(authCode);
        return ResponseEntity.ok(AuthResponse.builder().accessToken(response).build());
    }
}
