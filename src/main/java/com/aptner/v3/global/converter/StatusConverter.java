package com.aptner.v3.global.converter;

import com.aptner.v3.board.complain.ComplainStatus;
import com.aptner.v3.board.market.MarketStatus;
import com.aptner.v3.board.qna.QnaStatus;
import com.aptner.v3.board.qna.Status;
import jakarta.persistence.AttributeConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class StatusConverter implements Converter<String, Status> ,  AttributeConverter<Status, String> {
    @Override
    public Status convert(String source) {
        // Attempt to convert to each enum type
        Status status = getEnumFromCode(QnaStatus.class, source);
        if (status != null) {
            return status;
        }
        status = getEnumFromCode(MarketStatus.class, source);
        if (status != null) {
            return status;
        }
        status = getEnumFromCode(ComplainStatus.class, source);
        if (status != null) {
            return status;
        }
        throw new IllegalArgumentException("Invalid status: " + source);
    }

    private <E extends Enum<E> & Status> E getEnumFromCode(Class<E> enumClass, String code) {
        for (E status : enumClass.getEnumConstants()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /*============ jpa ================== */

    @Override
    public Status convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        Status status = getEntityAttributeEnumFromCode(QnaStatus.class, code);
        if (status != null) {
            return status;
        }

        status = getEntityAttributeEnumFromCode(MarketStatus.class, code);
        if (status != null) {
            return status;
        }

        status = getEntityAttributeEnumFromCode(ComplainStatus.class, code);
        if (status != null) {
            return status;
        }

        throw new IllegalArgumentException("Unknown status code: " + code);
    }

    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    private <E extends Enum<E> & Status> E getEntityAttributeEnumFromCode(Class<E> enumClass, String code) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /*============ jpa ================== */
}