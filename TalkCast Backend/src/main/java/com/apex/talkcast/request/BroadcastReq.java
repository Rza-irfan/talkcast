package com.apex.talkcast.request;

import com.apex.talkcast.enums.PrivacyStatus;
import com.google.api.services.youtube.model.MonitorStreamInfo;
import com.google.api.services.youtube.model.ThumbnailDetails;
import jakarta.mail.Multipart;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class BroadcastReq {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private PrivacyStatus privacyStatus;

    // Broadcast ContentDetails
    private Boolean enableEmbed;
    private Boolean enableAutoStart;
    private Boolean recordFromStart;

    // Stream CDN settings
    private String ingestionType; // "rtmp" or "dash"
    private String resolution;    // "720p", "1080p", etc.
    private String frameRate;     // "30fps", "60fps", etc.
    private Boolean isReusable;   // Stream contentDetails

    private MultipartFile thumbnail;

    private String channelId;
    private String lifecycleStatus;
    private String recordingStatus;
    private Boolean enableAutoStop;
}
