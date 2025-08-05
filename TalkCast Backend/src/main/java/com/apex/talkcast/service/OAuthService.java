package com.apex.talkcast.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OAuthService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.client.token-url}")
    private String tokenUrl;

    @Value("${google.client.redirect-uri}")
    private String redirectUrl;

    public String getAccessToken(String authCode) {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            var tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    JSON_FACTORY,
                    tokenUrl,
                    clientId,
                    clientSecret,
                    authCode,
                    redirectUrl
            ).setGrantType("authorization_code").execute();

            return tokenResponse.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
