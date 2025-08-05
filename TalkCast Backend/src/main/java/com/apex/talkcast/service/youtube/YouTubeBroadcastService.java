package com.apex.talkcast.service.youtube;

import com.apex.talkcast.enums.BroadcastStatus;
import com.apex.talkcast.request.BroadcastReq;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;

import static com.apex.talkcast.utils.ConverterUtils.toGoogleDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeBroadcastService {

    public LiveBroadcast createBroadcast(BroadcastReq req, YouTube youtube){
        try {
            LiveBroadcast broadcast = new LiveBroadcast()
                    .setSnippet(buildBroadcastSnippet(req))
                    .setContentDetails(buildContentDetails(req))
                    .setStatus(new LiveBroadcastStatus().setPrivacyStatus(req.getPrivacyStatus().getValue()));

            broadcast = youtube.liveBroadcasts()
                    .insert("snippet,contentDetails,status", broadcast)
                    .execute();
            return broadcast;
        } catch (Exception e) {
            log.error("Failed to create broadcast", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }




    /**
     * Builds the metadata for the Live Broadcast (the event).
     * This includes title, description, and scheduled times.
     */
    private LiveBroadcastSnippet buildBroadcastSnippet(BroadcastReq req) {
        LiveBroadcastSnippet snippet = new LiveBroadcastSnippet()
                .setTitle(req.getTitle())
                .setScheduledStartTime(toGoogleDateTime(req.getStartTime(), ZoneId.of("UTC")));

        if (req.getEndTime() != null) {
            snippet.setScheduledEndTime(toGoogleDateTime(req.getEndTime(), ZoneId.of("UTC")));
        }

        if (req.getDescription() != null) {
            snippet.setDescription(req.getDescription());
        }

        return snippet;
    }

    private LiveBroadcastContentDetails buildContentDetails(BroadcastReq req) {
        LiveBroadcastContentDetails details = new LiveBroadcastContentDetails();
        if (req.getEnableEmbed() != null) details.setEnableEmbed(req.getEnableEmbed());
        if (req.getEnableAutoStart() != null) details.setEnableAutoStart(req.getEnableAutoStart());
        if (req.getRecordFromStart() != null) details.setRecordFromStart(req.getRecordFromStart());
        return details;
    }


    public void uploadThumbnail(YouTube youtube, String videoId, MultipartFile file) throws IOException {
        try (InputStream imageStream = file.getInputStream()) {
            InputStreamContent media = new InputStreamContent(file.getContentType(), imageStream);
            youtube.thumbnails().set(videoId, media).execute();
        }
    }


}
