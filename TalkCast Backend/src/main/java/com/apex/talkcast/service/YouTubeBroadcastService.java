package com.apex.talkcast.service;

import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastContentDetails;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import java.util.Arrays;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class YouTubeBroadcastService {

    private static final String APPLICATION_NAME = "StreamYard Clone";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private final RestClient restClient = RestClient.create();


    public void createBroadcast(String code) throws Exception {
//        var tokenResponse = new GoogleAuthorizationCodeTokenRequest(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                "https://oauth2.googleapis.com/token",
//                CLIENT_ID,
//                CLIENT_SECRET,
//                code,
//                REDIRECT_URI
//        ).setGrantType("authorization_code").execute();
        //String refreshToken = getRefreshToken(tokenResponse.getRefreshToken());
//        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
//                .setAccessToken(tokenResponse.getAccessToken());

//        YouTube youtubeService = new YouTube.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JSON_FACTORY,
//                credential
//        ).setApplicationName(APPLICATION_NAME).build();

        LiveBroadcastSnippet snippet = new LiveBroadcastSnippet();
        snippet.setTitle("Nimbuda Nimbuda Nimbuda");
        snippet.setScheduledStartTime(new DateTime(System.currentTimeMillis() + 60000));
        snippet.setScheduledEndTime(new DateTime(System.currentTimeMillis() + 3600000));

        LiveBroadcastContentDetails contentDetails = new LiveBroadcastContentDetails();
        contentDetails.setEnableEmbed(true);

        LiveBroadcastStatus status = new LiveBroadcastStatus();
        status.setPrivacyStatus("public");

        LiveBroadcast broadcast = new LiveBroadcast();
        broadcast.setSnippet(snippet);
        broadcast.setContentDetails(contentDetails);
        broadcast.setStatus(status);

//        YouTube.LiveBroadcasts.Insert request = youtubeService.liveBroadcasts()
//                .insert("snippet,contentDetails,status", broadcast);
//        return request.execute();
    }




}
