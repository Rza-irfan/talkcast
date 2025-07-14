package com.apex.talkcast.controller;

import com.apex.talkcast.service.YouTubeBroadcastService;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.gson.Gson;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GoogleOAuthController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.client.redirect-uri}")
    private String redirectUri;

    private final YouTubeBroadcastService youTubeBroadcastService;

    @GetMapping("/auth/google")
    public String redirectToGoogle() {
        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile%20https://www.googleapis.com/auth/youtube" +
                "&access_type=offline&prompt=consent";

        return "<a href=\"" + oauthUrl + "\">Login with Google</a>";
    }

    @PostMapping("/oauth2/callback")
    public String oauth2Callback(@RequestParam("code") String code) throws Exception {
        // Call broadcast API with access token
        String accessToken = exchangeCodeForAccessToken(code);
        Date startTime = new Date(System.currentTimeMillis() + 600_000); // 10 min later
        String broadcastId = youTubeBroadcastService.createScheduledBroadcast("My YouTube Live", startTime, accessToken);

        return "Broadcast created with ID: " + broadcastId;
    }

    private String exchangeCodeForAccessToken(String code) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                "https://oauth2.googleapis.com/token",

                "Bearer "+code,
                "http://localhost:8080/oauth2/callback"  // Must match redirect URI registered in Google Console
        ).execute();

        return tokenResponse.getAccessToken();
    }
}

