package com.apex.talkcast.service.youtube;

import com.apex.talkcast.factory.VideoFactory;
import com.apex.talkcast.factory.VideoStatusFactory;
import com.apex.talkcast.request.YoutubeVideoUploadReq;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeUploadService {

    private final VideoStatusFactory videoStatusFactory;
    private final VideoFactory videoFactory;

    @Async
    public CompletableFuture<Video> uploadVideoAsync(
            YoutubeVideoUploadReq request,
            InputStream videoStream,
            String mimeType,
            long contentLength,
            YouTube youtubeClient) {

        try {
            // Prepare metadata
            VideoSnippet snippet = new VideoSnippet();
            snippet.setTitle(request.title());
            snippet.setDescription(request.description());
            snippet.setTags(request.tags());
            snippet.setCategoryId(request.categoryId());

            validateInputs(snippet, request.privacyStatus(), videoStream, mimeType, contentLength);

            // Create video status and full metadata
            VideoStatus status = videoStatusFactory.create(request.privacyStatus());
            Video videoMetadata = videoFactory.create(snippet, status);

            // Create media content
            InputStreamContent mediaContent = new InputStreamContent(mimeType, videoStream);
            mediaContent.setLength(contentLength);

            // Build insert request
            YouTube.Videos.Insert insertRequest = youtubeClient.videos()
                    .insert("snippet,status", videoMetadata, mediaContent);

            // Configure uploader
            MediaHttpUploader uploader = insertRequest.getMediaHttpUploader();
            uploader.setDirectUploadEnabled(request.directUpload());
            uploader.setChunkSize(10485760);

            uploader.setProgressListener(getDefaultProgressListener());

            // Execute upload
            Video uploadedVideo = insertRequest.execute();
            return CompletableFuture.completedFuture(uploadedVideo);

        } catch (IOException | IllegalArgumentException e) {
            log.error("Failed to upload video", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private void validateInputs(VideoSnippet snippet, String privacyStatus, InputStream videoStream,
                                String mimeType, long contentLength) {
        Objects.requireNonNull(snippet, "VideoSnippet must not be null");
        Objects.requireNonNull(privacyStatus, "privacyStatus must not be null");
        Objects.requireNonNull(videoStream, "Video input stream must not be null");
        Objects.requireNonNull(mimeType, "MIME type must not be null");

        if (contentLength <= 0) {
            throw new IllegalArgumentException("Content length must be positive");
        }

        if (!privacyStatus.equals("private") && !privacyStatus.equals("public") && !privacyStatus.equals("unlisted")) {
            throw new IllegalArgumentException("Invalid privacy status: " + privacyStatus);
        }
    }

    private MediaHttpUploaderProgressListener getDefaultProgressListener() {
        return uploader -> {
            switch (uploader.getUploadState()) {
                case INITIATION_STARTED ->
                        log.info("Upload initiation started");
                case INITIATION_COMPLETE ->
                        log.info("Upload initiation completed");
                case MEDIA_IN_PROGRESS ->
                        log.info("Upload in progress: {}%", (int) (uploader.getProgress() * 100));
                case MEDIA_COMPLETE ->
                        log.info("Upload completed successfully");
                case NOT_STARTED ->
                        log.info("Upload not started");
                default ->
                        log.warn("Unknown upload state: {}", uploader.getUploadState());
            }
        };
    }
}
