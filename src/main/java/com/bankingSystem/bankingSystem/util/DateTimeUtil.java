package com.bankingSystem.bankingSystem.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public static Timestamp stringToTimestamp(String dateTimeString) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
            LocalDate localDate = LocalDate.parse(dateTimeString, dateFormatter);
            return Timestamp.valueOf(localDate.atStartOfDay());
        }
    }

    public static Timestamp stringToTimestamp(String dateTimeString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date-time format: " + e.getMessage(), e);
        }
    }

    public static String timestampToString(Timestamp timestamp) {
        return timestampToString(timestamp, DEFAULT_DATE_TIME_PATTERN);
    }

    public static String timestampToString(Timestamp timestamp, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.format(formatter);
    }
}
