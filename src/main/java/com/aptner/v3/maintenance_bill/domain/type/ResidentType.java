package com.aptner.v3.maintenance_bill.domain.type;

import lombok.Getter;

@Getter
public enum ResidentType {
    RESIDENT("입주자");

    private final String korean;

    ResidentType(String korean) {
        this.korean = korean;
    }
}
