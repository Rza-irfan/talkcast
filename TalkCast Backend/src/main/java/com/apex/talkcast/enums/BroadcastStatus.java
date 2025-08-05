package com.apex.talkcast.enums;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Represents the status of a YouTube Live Broadcast.
 * Use this enum to define valid transition states.
 */
@Getter
public enum BroadcastStatus {

    CREATED("created"),
    READY("ready"),
    TESTING("testing"),
    LIVE("live"),
    COMPLETE("complete"); // 🔄 fixed typo from "completed"

    /**
     * -- GETTER --
     *
     * @return the string representation used by YouTube APIs.
     */
    private final String value;

    private static final Map<String, BroadcastStatus> VALUE_MAP =
            Stream.of(values())
                    .collect(Collectors.toUnmodifiableMap(BroadcastStatus::getValue, status -> status));

    BroadcastStatus(String value) {
        this.value = value;
    }

    /**
     * Safely parses a string to a BroadcastStatus.
     *
     * @param value the string value (e.g. "live", "created")
     * @return Optional containing BroadcastStatus if found
     */
    public static Optional<BroadcastStatus> fromValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(value));
    }

    @Override
    public String toString() {
        return value;
    }
}
