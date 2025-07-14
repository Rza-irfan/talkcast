package com.apex.talkcast.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class YouTubeBroadcastService {

    private static final String APPLICATION_NAME = "StreamYard Clone";
//    private static final JsonFactory JSON_FACTORY = JsonFactory.getDefaultInstance();

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public String createScheduledBroadcast(String title, Date scheduledStartTime, String accessToken) {
        try {
            YouTube youtubeService = getService(accessToken);

            LiveBroadcastSnippet snippet = new LiveBroadcastSnippet()
                    .setTitle(title)
                    .setScheduledStartTime(new DateTime(scheduledStartTime));

            LiveBroadcastStatus status = new LiveBroadcastStatus()
                    .setPrivacyStatus("public"); // Options: "public", "unlisted", "private"

            LiveBroadcast broadcast = new LiveBroadcast()
                    .setSnippet(snippet)
                    .setStatus(status);

            LiveBroadcast response = youtubeService.liveBroadcasts()
                    .insert("snippet,status", broadcast)
                    .execute();

            log.info("Scheduled broadcast created with ID: {}", response.getId());
            return response.getId();

        } catch (Exception e) {
            log.error("Error creating scheduled broadcast: {}", e.getMessage(), e);
            return null;
        }
    }

    private YouTube getService(String accessToken) throws GeneralSecurityException, IOException {
        return new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                request -> request.getHeaders().setAuthorization("Bearer " + accessToken)
        ).setApplicationName(APPLICATION_NAME).build();
    }

}
