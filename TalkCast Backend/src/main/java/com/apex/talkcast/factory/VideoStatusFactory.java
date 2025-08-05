package com.apex.talkcast.factory;

import com.google.api.services.youtube.model.VideoStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoStatusFactory {
    public VideoStatus create(String privacyStatus) {
        Objects.requireNonNull(privacyStatus, "privacyStatus must not be null");
        return new VideoStatus().setPrivacyStatus(privacyStatus);
    }
}

