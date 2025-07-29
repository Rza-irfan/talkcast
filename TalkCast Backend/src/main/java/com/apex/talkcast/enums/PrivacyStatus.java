package com.apex.talkcast.enums;

public enum PrivacyStatus {
    PUBLIC("public"),
    PRIVATE("private"),
    UNLISTED("unlisted");

    private final String value;

    PrivacyStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
