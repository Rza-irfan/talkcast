package com.apex.talkcast.request;

import java.util.List;

public record YoutubeVideoUploadReq(
        String title,
        String description,
        List<String> tags,
        String categoryId,
        String privacyStatus,
        boolean directUpload,
        int chunkSize
) {}
