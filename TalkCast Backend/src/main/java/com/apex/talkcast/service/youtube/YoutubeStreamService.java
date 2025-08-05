package com.apex.talkcast.service.youtube;

import com.apex.talkcast.enums.BroadcastStatus;
import com.apex.talkcast.request.BroadcastReq;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.apex.talkcast.utils.ConverterUtils.defaultIfNull;
import static com.apex.talkcast.utils.YoutubeConstants.*;
import static com.apex.talkcast.utils.YoutubeConstants.DEFAULT_IS_REUSABLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeStreamService {

    /**
     * Broadcast lifecycle transitions (in order):
     *
     * <pre>
     * +-----------+--------------------------+
     * | From      | Valid Transitions To     |
     * +-----------+--------------------------+
     * | created   | ready                    |
     * | ready     | testing, live            |
     * | testing   | live, complete           |
     * | live      | complete                 |
     * | complete  | (no further transitions) |
     * +-----------+--------------------------+
     * </pre>
     *
     * Status definitions:
     * - created: Broadcast is created but not yet ready to receive streams.
     * - ready: Bound to a stream and ready to receive RTMP data.
     * - testing: Stream is being sent, and you can preview before going live.
     * - live: Broadcast is actively streaming and public (or per its privacy setting).
     * - complete: Broadcast has ended and cannot be restarted.
     *
     * Note:
     * - You *must* send RTMP data before transitioning to "testing" or "live".
     * - Failing to send a stream results in "errorStreamInactive" (403).
     */

    /**
     * Transitions a YouTube Live Broadcast to a new lifecycle status.
     *
     * @param youtube     Initialized YouTube API client
     * @param broadcastId ID of the broadcast to transition
     * @param status      Target lifecycle status to transition to
     * @return Updated status as returned by YouTube
     * @throws IllegalStateException if the transition fails due to API restrictions or internal error
     */
    public String updateBroadcastStatus(YouTube youtube, String broadcastId, BroadcastStatus status) {
        if (youtube == null) {
            throw new IllegalArgumentException("YouTube API client must not be null.");
        }
        if (broadcastId == null || broadcastId.isBlank()) {
            throw new IllegalArgumentException("Broadcast ID must not be null or blank.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Target broadcast status must be provided.");
        }
        try {
            log.info("Attempting to transition broadcast [{}] to [{}]...", broadcastId, status);

            LiveBroadcast result = performTransition(youtube, broadcastId, status);
            String updatedStatus = result.getStatus().getLifeCycleStatus();

            log.info("Broadcast [{}] successfully transitioned to [{}]", broadcastId, updatedStatus);
            return updatedStatus;

        } catch (GoogleJsonResponseException ex) {
            handleGoogleJsonError(ex, broadcastId, status);
            throw new IllegalStateException("This line should not be reachable; error already thrown above.");
        } catch (Exception e) {
            log.error("Unexpected error while transitioning broadcast [{}] to [{}]", broadcastId, status, e);
            throw new IllegalStateException("Unexpected error during broadcast transition", e);
        }
    }


    /**
     * Performs the actual status transition API call to YouTube.
     */
    private LiveBroadcast performTransition(YouTube youtube, String broadcastId, BroadcastStatus status) throws Exception {
        return youtube.liveBroadcasts()
                .transition(status.getValue(), broadcastId, "status")
                .execute();
    }

    public LiveStream createLiveStream(YouTube youtube, BroadcastReq req) throws IOException {
        LiveStreamSnippet snippet = new LiveStreamSnippet().setTitle(req.getTitle());

        CdnSettings cdn = new CdnSettings()
                .setIngestionType(defaultIfNull(req.getIngestionType(), DEFAULT_INGESTION_TYPE))
                .setResolution(defaultIfNull(req.getResolution(), DEFAULT_RESOLUTION))
                .setFrameRate(defaultIfNull(req.getFrameRate(), DEFAULT_FRAME_RATE));

        LiveStreamContentDetails contentDetails = new LiveStreamContentDetails()
                .setIsReusable(defaultIfNull(req.getIsReusable(), DEFAULT_IS_REUSABLE));

        LiveStream stream = new LiveStream()
                .setSnippet(snippet)
                .setCdn(cdn)
                .setContentDetails(contentDetails);

        return youtube.liveStreams().insert("snippet,cdn,contentDetails", stream).execute();
    }

    /**
     * Handles common YouTube API errors and throws more user-friendly messages.
     */
    private void handleGoogleJsonError(GoogleJsonResponseException ex, String broadcastId, BroadcastStatus status) {
        String reason = ex.getDetails().getErrors().getFirst().getReason();
        String message;

        switch (reason) {
            case "errorStreamInactive" ->
                    message = "Cannot transition to '" + status + "' — RTMP stream is inactive. Make sure you're sending data.";
            case "forbidden" ->
                    message = "Transition to '" + status + "' is forbidden. Ensure the broadcast is bound to a valid stream.";
            case "invalidTransition" ->
                    message = "Invalid lifecycle transition from current state to '" + status + "'. Check the lifecycle rules.";
            default ->
                    message = "YouTube API error: " + ex.getDetails().getMessage();
        }

        log.error("Failed to transition broadcast [{}] to [{}]: {}", broadcastId, status, message);
        throw new IllegalStateException(message, ex);
    }
}
