package com.aptner.v3.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
        Timestamp result = (locDateTime == null) ? null : Timestamp.valueOf(locDateTime);
        log.debug("converted -> timestamp : {}", result);
        return result;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        LocalDateTime result = sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime();
        log.debug("converted -> LocalDateTime : {}", result);
        return result;
    }
}
