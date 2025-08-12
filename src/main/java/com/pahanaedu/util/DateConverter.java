package com.pahanaedu.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private final DateTimeFormatter TS = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public String toStringLdt(LocalDateTime ldt) {
        return (ldt == null) ? null : ldt.format(TS);
    }

    public LocalDateTime toLdt(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return LocalDateTime.parse(s, TS);
        } catch (Exception ignore) {
        }
        try {
            return java.time.LocalDate.parse(s).atStartOfDay();
        } catch (Exception ignore) {
        }
        return LocalDateTime.parse(s, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
