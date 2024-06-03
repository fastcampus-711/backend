package com.aptner.v3.maintenance_bill.domain.type;

import lombok.Getter;

@Getter
public enum HouseType {
    APARTMENT("아파트"),
    DETACHED_HOUSE("주택");

    private final String korean;

    HouseType(String korean) {
        this.korean = korean;
    }

}
