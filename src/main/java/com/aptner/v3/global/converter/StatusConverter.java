package com.aptner.v3.global.converter;

import com.aptner.v3.board.complain.ComplainStatus;
import com.aptner.v3.board.market.MarketStatus;
import com.aptner.v3.board.qna.QnaStatus;
import com.aptner.v3.board.qna.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StatusConverter implements Converter<String, Status> {
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
}