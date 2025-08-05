package com.apex.talkcast.utils;

import com.google.api.client.util.DateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ConverterUtils {

    public static DateTime toGoogleDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        long millis = localDateTime.atZone(zoneId).toInstant().toEpochMilli();
        return new DateTime(millis);
    }

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

}
