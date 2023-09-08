package ru.practical.work.entity;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeSerializer extends StdConverter<LocalDateTime, Long> {
    @Override
    public Long convert(LocalDateTime dateTime) {
        if (dateTime == null)
            return 0L;
        else
            return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}