package com.aptner.v3.maintenance_bill.embed.maintenance_bill_monthly_static;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthOnMonthInfo {
    private long maintenanceFeeOfPresent;
    private long maintenanceFeeOfLastMonth;
    private long maintenanceFeeOfTwoMonthsAgo;
}
