package com.apex.talkcast.factory;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoFactory {
    public Video create(VideoSnippet snippet, VideoStatus status) {
        Objects.requireNonNull(snippet, "VideoSnippet must not be null");
        Objects.requireNonNull(status, "VideoStatus must not be null");
        return new Video().setSnippet(snippet).setStatus(status);
    }
}
