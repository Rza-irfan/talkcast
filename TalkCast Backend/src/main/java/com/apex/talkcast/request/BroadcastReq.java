package com.apex.talkcast.request;

import com.apex.talkcast.enums.PrivacyStatus;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BroadcastReq {

    // === OAuth ===
    @NonNull
    @NotBlank(message = "Authorization code is required.")
    private final String authCode;

    // === Snippet Metadata ===
    @NonNull
    @NotBlank(message = "Broadcast title is required.")
    private final String title;

    @NotNull(message = "Scheduled start time is required.")
    @NonNull
    private final LocalDateTime startTime;

    @NotBlank(message = "Privacy status must be one of: public, private, or unlisted.")
    @NonNull
    private final PrivacyStatus privacyStatus;

    // === Optional Fields ===
    private String description;
    //TODO: will work on it
//    private List<String> tags;
    private LocalDateTime endTime;

    // === ContentDetails Options ===
    private Boolean enableEmbed;
    private Boolean enableAutoStart;
    private Boolean enableAutoStop;
    private Boolean recordFromStart;
//    private MonitorStreamInfo monitorStream;
}
