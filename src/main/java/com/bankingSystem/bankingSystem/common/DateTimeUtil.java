package com.bankingSystem.bankingSystem.common;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    private DateTimeUtil() {
        throw new UnsupportedOperationException();
    }

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static Timestamp stringToTimestamp(String dateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
        try {
            LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
            return Timestamp.valueOf(localDate.atStartOfDay());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public static Timestamp stringToTimestamp(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return Timestamp.valueOf(localDate.atStartOfDay());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException();
        }
    }

    private static String timestampToString(Timestamp timestamp) {
        return timestampToString(timestamp, DEFAULT_DATE_PATTERN);
    }

    private static String timestampToString(Timestamp timestamp, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        return localDate.format(formatter);
    }
}
