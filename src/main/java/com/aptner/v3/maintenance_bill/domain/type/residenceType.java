package com.aptner.v3.maintenance_bill.domain.type;

import lombok.Getter;

@Getter
public enum residenceType {
    OWNER("자가"),
    MONTHLY_RENTER("월세"),
    YEARLY_RENTER("전세");

    private String korean;

    residenceType(String korean) {
        this.korean = korean;
    }
}
