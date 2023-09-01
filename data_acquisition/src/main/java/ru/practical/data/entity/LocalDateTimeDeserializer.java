package ru.practical.data.entity;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer extends StdConverter<Long, LocalDateTime> {
    @Override
    public LocalDateTime convert(Long dateTimeInSeconds) {
        if (dateTimeInSeconds == 0)
            return null;
        else
            return Instant.ofEpochMilli(dateTimeInSeconds * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}