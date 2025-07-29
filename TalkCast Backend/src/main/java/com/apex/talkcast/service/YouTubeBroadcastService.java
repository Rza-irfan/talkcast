package com.apex.talkcast.service;

import com.apex.talkcast.request.BroadcastReq;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

import static com.apex.talkcast.utils.ConverterUtils.toGoogleDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeBroadcastService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "TalkCast";

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.client.token-url}")
    private String tokenUrl;

    @Value("${google.client.redirect-uri}")
    private String redirectUrl;

    public String createBroadcast(BroadcastReq request) throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredential(httpTransport, request);
        YouTube youtubeService = createYouTubeService(httpTransport, credential);

        LiveBroadcast broadcast = new LiveBroadcast()
                .setSnippet(buildSnippet(request))
                .setContentDetails(buildContentDetails(request))
                .setStatus(buildStatus(request));

        LiveBroadcast response = youtubeService.liveBroadcasts()
                .insert("snippet,contentDetails,status", broadcast)
                .execute();

        log.info("Broadcast created: ID={}, Title={}", response.getId(), response.getSnippet().getTitle());
        return response.getId();
    }

    private Credential getCredential(NetHttpTransport httpTransport, BroadcastReq request) throws Exception {
        var tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                JSON_FACTORY,
                tokenUrl,
                clientId,
                clientSecret,
                request.getAuthCode(),
                redirectUrl
        ).setGrantType("authorization_code").execute();

        return new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(tokenResponse.getAccessToken());
    }

    private YouTube createYouTubeService(NetHttpTransport httpTransport, Credential credential) {
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private LiveBroadcastSnippet buildSnippet(BroadcastReq request) {
        LiveBroadcastSnippet snippet = new LiveBroadcastSnippet()
                .setTitle(request.getTitle())
                .setScheduledStartTime(toGoogleDateTime(request.getStartTime(), ZoneId.of("UTC")));

        if (request.getEndTime() != null) {
            snippet.setScheduledEndTime(toGoogleDateTime(request.getEndTime(), ZoneId.of("UTC")));
        }

        if (request.getDescription() != null) {
            snippet.setDescription(request.getDescription());
        }

        return snippet;
    }

    private LiveBroadcastContentDetails buildContentDetails(BroadcastReq request) {
        LiveBroadcastContentDetails contentDetails = new LiveBroadcastContentDetails();

        if (request.getEnableEmbed() != null) {
            contentDetails.setEnableEmbed(request.getEnableEmbed());
        }

        if (request.getEnableAutoStart() != null) {
            contentDetails.setEnableAutoStart(request.getEnableAutoStart());
        }

        if (request.getRecordFromStart() != null) {
            contentDetails.setRecordFromStart(request.getRecordFromStart());
        }

        // Placeholder for future implementation
//        if (request.getMonitorStream() != null) {
//            contentDetails.setMonitorStream(request.getMonitorStream());
//        }

        return contentDetails;
    }

    private LiveBroadcastStatus buildStatus(BroadcastReq request) {
        return new LiveBroadcastStatus()
                .setPrivacyStatus(request.getPrivacyStatus().getValue());
    }
}
