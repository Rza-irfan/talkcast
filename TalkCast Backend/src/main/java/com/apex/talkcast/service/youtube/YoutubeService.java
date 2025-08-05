package com.apex.talkcast.service.youtube;

import com.apex.talkcast.request.BroadcastReq;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.PlaylistListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final YouTubeBroadcastService youtubeBroadcastService;
    private final YoutubeStreamService youtubeStreamService;

    public String scheduleStream(BroadcastReq req, YouTube youtubeClient) {
        try {

            LiveBroadcast broadcast = youtubeBroadcastService.createBroadcast(req, youtubeClient);

            // Step 2: Upload Thumbnail (optional)
            if (req.getThumbnail() != null) {
                youtubeBroadcastService.uploadThumbnail(youtubeClient, broadcast.getId(), req.getThumbnail());
            }

            // Step 3: Create Stream
            LiveStream stream = youtubeStreamService.createLiveStream(youtubeClient, req);

            // Step 4: Bind Stream to Broadcast
            bindStreamToBroadcast(youtubeClient, broadcast.getId(), stream.getId());

            log.info("Broadcast created: ID={}, Title={}", broadcast.getId(), req.getTitle());
            return stream.getId();
        } catch (Exception e) {
            log.error("Failed to create broadcast", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    private void bindStreamToBroadcast(YouTube youtube, String broadcastId, String streamId) throws IOException {
        youtube.liveBroadcasts()
                .bind(broadcastId, "id,contentDetails")
                .setStreamId(streamId)
                .execute();
    }

    public PlaylistListResponse getPlaylists(YouTube youtube) throws IOException {
        YouTube.Playlists.List request = youtube.playlists()
                .list("snippet,contentDetails")
                .setMine(true)
                .setMaxResults(10L);

        return request.execute();
    }



//    public List<Map<String, String>> fetchUserPlaylists(String accessToken){
//        URI uri = UriComponentsBuilder.fromUriString("https://www.googleapis.com/youtube/v3/playlists")
//                .queryParam("part", "snippet")
//                .queryParam("mine", "true")
//                .queryParam("maxResults", 25)
//                .build()
//                .toUri();
//
//        String responseBody = restClient.get()
//                .uri(uri)
//                .header("Authorization", "Bearer " + accessToken)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(String.class);
//
//        JsonNode root = objectMapper.readTree(responseBody);
//        List<Map<String, String>> playlists = new ArrayList<>();
//
//        for (JsonNode item : root.path("items")) {
//            String id = item.path("id").asText();
//            String title = item.path("snippet").path("title").asText();
//            playlists.add(Map.of("id", id, "title", title));
//        }
//
//        return playlists;
//    }
}
